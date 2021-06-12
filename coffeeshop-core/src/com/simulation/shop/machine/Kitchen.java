package com.simulation.shop.machine;

public enum Kitchen {

	INSTANCE;

	private GrinderMachine grinderMachine;
	private EspressoMachine espressoMachine;
	private SteamerMachine steamerMachine;

	private Kitchen() {
		grinderMachine = new GrinderMachine();
		espressoMachine = new EspressoMachine();
		steamerMachine = new SteamerMachine();
	}

	public GrinderMachine getGrinderMachine() {
		return grinderMachine;
	}

	public void setGrinderMachine(GrinderMachine grinderMachine) {
		this.grinderMachine = grinderMachine;
	}

	public EspressoMachine getEspressoMachine() {
		return espressoMachine;
	}

	public void setEspressoMachine(EspressoMachine espressoMachine) {
		this.espressoMachine = espressoMachine;
	}

	public SteamerMachine getSteamerMachine() {
		return steamerMachine;
	}

	public void setSteamerMachine(SteamerMachine steamerMachine) {
		this.steamerMachine = steamerMachine;
	}

}
