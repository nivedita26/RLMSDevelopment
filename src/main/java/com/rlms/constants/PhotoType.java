package com.rlms.constants;

public enum PhotoType {

	MACHINE_PHOTO(1, "machinePhoto"),
	PANEL_PHOTO(2,"panelPhoto"),
	ARD_PHOTO(3,"ARDPhoto"),
	COP_PHOTO(4,"COPPhoto"),
	LOP_PHOTO(5,"LOPPhoto"),
	CARTOP_PHOTO(6,"cartopPhoto"),
	AUTO_DOOR_HEADER_PHOTO(7, "autoDoorHeaderPhoto"),
	WIRING_PHOTO(8,"wiringPhoto"),
	LOBBY_PHOTO(9,"lobbyPhoto");
	
	private int id;
	private String name;
	
	PhotoType(int id, String name){
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
