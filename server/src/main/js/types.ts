
export interface Weatherdata {
	timestamp?: number;
	temperature: number;
	humidity: number;
	pressure: number;
	voltage: number;
}

export const enum EspOperatingMode {
	ESP_MODE_HIBERNATE,
	ESP_MODE_DEEPSLEEP,
}

export interface NodeConfig {
	sendDataInterval: number;
	measureInterval: number;
	espOperatingMode: EspOperatingMode;
	useULP: boolean;
	sendDataDeltas: Weatherdata
	swCutoffVoltage: number;
}

export interface shortDataBattery {
	voltage: number;
	batteryPercentage: number;
	remainingTime: number;
}

export interface shortData {
	battery: shortDataBattery;
	latestDataSet: Weatherdata;
	firstRegistered: number;
}

export interface Node {
	id: string;
	mac: string;
	displayName: string;
	config: NodeConfig;
	shortData?: shortData;
	datasinksConfigs: datasinkConfig[];
}

export interface datasinkConfig {
	id: string;
	datasink: datasink;
	datasinkConfig: any;
}

export interface datasink {
	id: string ;
	sinkId: string;
	displayName: string
}