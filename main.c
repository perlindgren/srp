/*
===============================================================================
 Name        : main.c
 Author      : 
 Version     :
 Copyright   : Copyright (C) 
 Description : main definition
===============================================================================
*/

#ifdef __USE_CMSIS
#include "LPC11xx.h"
#endif

#include <cr_section_macros.h>
#include <NXP/crp.h>

// Variable to store CRP value in. Will be placed automatically
// by the linker when "Enable Code Read Protect" selected.
// See crp.h header for more information
__CRP const unsigned int CRP_WORD = CRP_NO_CRP ;

// TODO: insert other include files here

// TODO: insert other definitions and declarations here

#define MIN(X,Y) ((X) < (Y) ? (X) : (Y))
#define MAX(X,Y) ((X) > (Y) ? (X) : (Y))
/*
#define MIN(a,b) \
   ({ __typeof__ (a) _a = (a); \
       __typeof__ (b) _b = (b); \
     _a < _b ? _a : _b; })
*/


// Target Specific Macros

/* Source Priority Patch Scheduler */
#define CLAIM(R,CODE) { 					\
	NVIC_DisableIRQ(JOB_IRQN);				\
	int sc_old = sc; 						\
	sc = MAX(sc, R##_Ceiling);				\
	NVIC_SetPriority(JOB_IRQN, H(sc)); 		\
	{CODE}; 								\
	sc = sc_old;							\
	NVIC_SetPriority(JOB_IRQN, H(sc)); 	    \
	NVIC_EnableIRQ(JOB_IRQN);	    		\
}


/*
#define CLAIM(R,CODE) { 					\
	int sc_old = sc; 						\
	sc = MAX(sc, R##_Ceiling);				\
	NVIC->ICER[0] = LockMask[sc];			\
	{CODE}; 								\
	sc = sc_old;							\
	NVIC->ISER[0] = UnlockMask[sc];		    \
}
*/

#define PEND(J) 	{ NVIC_SetPendingIRQ(J##_IRQn); }
#define UNPEND(J) 	{ NVIC_ClrPendingIRQ(J##_IRQn); }

// M0/M1
#define H(X) (3-X)

// Application Specific Macros
#define oa_Ceiling 2
#define ob_Ceiling 3

#define j1_Prio 1
#define j2_Prio 2
#define j3_Prio 3

/*
#define j1_IRQn EINT1_IRQn
#define j2_IRQn EINT2_IRQn
#define j3_IRQn EINT3_IRQn
*/


#define j1_IRQn TIMER_16_0_IRQn
#define j2_IRQn TIMER_32_0_IRQn
#define j3_IRQn TIMER_32_1_IRQn



// Application specific
#define J4 (0)
#define J3 (1 << j3_IRQn)
#define J2 (1 << j2_IRQn)
#define J1 (1 << j1_IRQn)

// General
int UnlockMask[5]={
		(J1 | J2 | J3 | J4),							/* prio 0, idle */
		(J2 | J3 | J4 ),								/* prio 1, lowest priority */
		(J3 | J4), 										/* prio 2 */
		(J4), 											/* prio 3 */
		(0)		 										/* prio 4, highest */
};

int LockMask[5]={
		~(J1 | J2 | J3 | J4),							/* prio 0, idle */
		~(J2 | J3 | J4 ),								/* prio 1, lowest priority */
		~(J3 | J4), 									/* prio 2 */
		~(J4), 											/* prio 3 */
		~(0)		 									/* prio 4, highest */
};


void job1_code(void);
void job2_code(void);
void job3_code(void);

/*
void EINT1_IRQHandler() { job1_code(); }
void EINT2_IRQHandler() { job2_code(); }
void EINT3_IRQHandler() { job3_code(); }
*/


void TIMER_16_0_IRQHandler() { job1_code(); }
void TIMER_32_0_IRQHandler() { job2_code(); }
void TIMER_32_1_IRQHandler() { job3_code(); }

volatile int sc = 0; // Initial system ceiling = 0 = idle

void SRP_SETUP() {
	// Enable sources
	NVIC_EnableIRQ(j1_IRQn);
	NVIC_EnableIRQ(j2_IRQn);
	NVIC_EnableIRQ(j3_IRQn);

	// Set priority
	NVIC_SetPriority(j1_IRQn, H(j1_Prio)); /* lowest */
	NVIC_SetPriority(j2_IRQn, H(j2_Prio)); /* mid */
	NVIC_SetPriority(j3_IRQn, H(j3_Prio)); /* high */
}

void wait(int j) {
	volatile int i,k;
	for (i = 0; i < j; i++) {
		k++;
	}
}

volatile int bp;

//#define JOB_IRQN EINT1_IRQn
#define JOB_IRQN j1_IRQn
void job1_code() {
	bp = 0;
	CLAIM(oa, {wait(100);});
}


#undef JOB_IRQN
#define JOB_IRQN j2_IRQn
void job2_code() {
	bp = 1;
	CLAIM(ob,
		wait(100);
		CLAIM(oa,
				PEND(j1);
				PEND(j3);
				wait(100);
		);
		wait(100);
	);
}


#undef JOB_IRQN
#define JOB_IRQN j3_IRQn
void job3_code() {
	bp =2;
	CLAIM(ob,
		wait(100);
	);
}

volatile int i;

int main(void) {
	__disable_irq();
	SRP_SETUP();
	//PEND(j1);
	PEND(j2);
	//PEND(j3);
	__enable_irq();

	while (1) {
		i++;
	}
}
