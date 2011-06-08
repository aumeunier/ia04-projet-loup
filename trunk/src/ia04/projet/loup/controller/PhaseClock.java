package ia04.projet.loup.controller;

import ia04.projet.loup.Global;
import ia04.projet.loup.controller.AgtStoryteller.GameExitErrorCodes;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class is the clock used by the system to determine when to go to the next phases
 * @author aurelien
 *
 */
public class PhaseClock {
	private static final int PREPARATION_PHASE_DURATION = 5000;
	/** The timer used to time the phases */
	private Timer timer;
	/** Is the clock on ? */
	private boolean isTimerRunning;
	/** The number of turns that have been played so far */
	private int nbOfTurns;
	/**
	 * Reference to the storyteller that controls the clock. 
	 * This variable is used to send messages to the storyteller.
	 */
	private AgtStoryteller storyteller;
	/** The current phase of the timer */
	private Global.GamePhases currentPhase = Global.GamePhases.NONE;
	/** How long should the phase be ? */
	private int previousPhaseDuration;

	/**
	 * Default constructor to use.  
	 * @param s The Storyteller Agent that wants to create this PhaseClock and use it as internal clock
	 */
	public PhaseClock(AgtStoryteller s){
		super();
		this.nbOfTurns = -1;
		this.storyteller = s;
		this.timer = new Timer("phaseTimer");
		this.isTimerRunning = false;
	}	

	public void startPreparationTimer(){
		this.currentPhase = Global.GamePhases.NONE;
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				if(!isTimerRunning){
					storyteller.playersParticipationTimeElapsed();
				}
			}			
		}, PREPARATION_PHASE_DURATION);
	}
	public void startGameTimer(){
		this.nbOfTurns = 0;
		this.isTimerRunning = true;
		startNextPhase();
		//TODO: maybe a problem if timer was stopped before		
	}
	public void stopTimer(){
		this.isTimerRunning = false;
		timer.cancel();
	}
	public int getNbOfTurns(){
		return nbOfTurns;
	}
	public Global.GamePhases getCurrentPhase(){
		return currentPhase;
	}

	/**
	 * Start the next useful phase of the game.
	 */
	public void startNextPhase(){
		// Select the next phase
		int nextPhaseIndex = currentPhase.ordinal();
		do{
			nextPhaseIndex++;
			if(nextPhaseIndex >= Global.GamePhases.values().length){
				nextPhaseIndex = 1; // 0 is for NONE
			}
		} while(this.storyteller.shouldSkipPhase(Global.GamePhases.values()[nextPhaseIndex])
				&& nextPhaseIndex != currentPhase.ordinal());
		
		// If no phase should be executed, then the game is probably over
		if(nextPhaseIndex == currentPhase.ordinal()){
			this.storyteller.endGameWithState(GameExitErrorCodes.NO_POSSIBLE_PHASE);
		}

		// Beginning of a new phase
		else {
			setCurrentPhase(Global.GamePhases.values()[nextPhaseIndex]);	
		}
	}
	/**
	 * Set the new phase of the clock. Notify the Storyteller about the change
	 * @param clockPhase The phase to start right now
	 */
	private void setCurrentPhase(Global.GamePhases clockPhase){
		this.currentPhase = clockPhase;

		// PhaseClock operations
		int phaseDuration = 0;
		switch(currentPhase){
		case NONE:
			startNextPhase();
			return;
		case NIGHT:
			nbOfTurns++;
			phaseDuration = (int) (5000*Global.AVERAGE_SPEED);
			break;
		default:
			phaseDuration = (int) (5000*Global.AVERAGE_SPEED);
			break;				
		}
		
		// Notify the Storyteller of a phase change
		storyteller.willStartPhase(currentPhase);
		
		// Start a timer for the phase if necessary
		if(phaseDuration > 0 && this.isTimerRunning){
			timer.schedule(new TimerTask(){
				@Override
				public void run() {
					storyteller.endOfPhase(currentPhase);
				}			
			}, phaseDuration);
			this.previousPhaseDuration = phaseDuration;
		}
	}
	
	/**
	 * Used to give more time to the current phase
	 */
	public void restartPhaseTimer(){
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				storyteller.endOfPhase(currentPhase);
			}			
		}, previousPhaseDuration);
	}
}
