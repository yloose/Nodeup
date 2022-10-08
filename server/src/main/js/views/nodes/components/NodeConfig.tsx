import React, { useState } from "react";

import * as types from '../../../types';
import "../../../app.css";



const NodeConfig = function(props: { node: types.Node; updateNodeFunc: Function }) {
	let node = props.node;

	const [changed, setChanged] = useState(false);

	function updateNode(e) {

		node.displayName = e.target.elements.displayName.value;
		node.config = {
			sendDataInterval: e.target.elements.sendDataInterval.value,
			measureInterval: e.target.elements.measureInterval.value,
			espOperatingMode: e.target.elements.espMode.value,
			useULP: e.target.elements.useULP.checked,
			sendDataDeltas: {
				temperature: e.target.elements.temperature.value,
				humidity: e.target.elements.humidity.value,
				pressure: e.target.elements.pressure.value,
				voltage: e.target.elements.voltage.value,
			},
			swCutoffVoltage: e.target.elements.swCutoffVoltage.value,
		}

		props.updateNodeFunc(e, node);
	}

	return (
		<div className="shadow-xl rounded-xl grow relative">
			<div className="ml-8 mt-2">
				<span className="text-3xl font-bold">Configuration</span>
				<span className="block text-gray-500">MAC: {node.mac}</span>
				<span className="block text-gray-500 text-sm">ID: {node.id}</span>
			</div>
			<form onSubmit={updateNode} className="ml-8">

				<div className="flex align-middle">
					<div>
						<div className="my-4">
							<label className="ml-1 text-base block text-gray-600" htmlFor="displayName">Name</label>
							<input onChange={e => setChanged(true)} className="border-gradient-primary border-2 border-transparent rounded-lg block leading-8 px-2" name="displayName" defaultValue={node.displayName} />
						</div>

						<div className="my-4">
							<label className="ml-1 text-base block text-gray-600" htmlFor="sendDataInterval">Send data interval</label>
							<input onChange={e => setChanged(true)} className="border-gradient-primary border-2 border-transparent rounded-lg [appearance:textfield] block leading-8 px-2" name="sendDataInterval" type="number" defaultValue={node.config.sendDataInterval} />
						</div>

						<div className="my-4">
							<label className="ml-1 text-base block text-gray-600" htmlFor="measureInterval">Measure interval</label>
							<input onChange={e => setChanged(true)} className="border-gradient-primary border-2 border-transparent rounded-lg block leading-8 px-2" name="measureInterval" type="number" defaultValue={node.config.measureInterval} />
						</div>
					</div>
					<div className="ml-12">
						<div className="my-4">
							<label className="ml-1 text-base block text-gray-600" htmlFor="espMode">ESP operating mode</label>
							<select className="leading-8 outline-none bg-background pl-2 pr-8 py-1 rounded-lg border-gradient-primary border-2 border-transparent" name="espMode" defaultValue={node.config.espOperatingMode}>
									<option value={types.EspOperatingMode.ESP_MODE_HIBERNATE}>Hibernate</option>
									<option value={types.EspOperatingMode.ESP_MODE_DEEPSLEEP}>Deep Sleep</option>
							</select>
						</div>

						<div className="my-4 flex items-center">
							<label className="ml-1 text-base text-gray-600" htmlFor="useULP">Use ULP</label>
							<input className="w-6 h-6 ml-8" type="checkbox" name="useULP" defaultChecked={node.config.useULP ? true : false} />
						</div>

						<div className="my-4">
							<label className="ml-1 text-base block text-gray-600" htmlFor="swCutoffVoltage">SW cutoff voltage</label>
							<input onChange={e => setChanged(true)} className="border-gradient-primary border-2 border-transparent rounded-lg block leading-8 px-2" name="swCutoffVoltage" type="number" defaultValue={node.config.swCutoffVoltage} />
						</div>
					</div>
					<div className="border-gradient-primary border-2 border-transparent rounded-lg ml-24">
						<span className="font-bold -translate-y-4 inline-block px-2 ml-2 bg-background">Send data deltas</span>
						<div className="-mt-4 mx-4">
							<label className="ml-1 text-base block text-gray-600" htmlFor="temperature">Temperature</label>
							<input onChange={e => setChanged(true)} className="border-gradient-grey border-2 border-transparent rounded-lg block leading-6 px-2 w-48" name="temperature" type="number" defaultValue={node.config.sendDataDeltas.temperature} />
						</div>
						<div className="my-2 mx-4">
							<label className="ml-1 text-base block text-gray-600" htmlFor="humidity">Humidity</label>
							<input onChange={e => setChanged(true)} className="border-gradient-grey border-2 border-transparent rounded-lg block leading-6 px-2 w-48" name="humidity" type="number" defaultValue={node.config.sendDataDeltas.humidity} />
						</div>
						<div className="my-2 mx-4">
							<label className="ml-1 text-base block text-gray-600" htmlFor="pressure">Pressure</label>
							<input onChange={e => setChanged(true)} className="border-gradient-grey border-2 border-transparent rounded-lg block leading-6 px-2 w-48" name="pressure" type="number" defaultValue={node.config.sendDataDeltas.pressure} />
						</div>
						<div className="my-2 mx-4 mb-4">
							<label className="ml-1 text-base block text-gray-600" htmlFor="voltage">Voltage</label>
							<input step={0.05} onChange={e => setChanged(true)} className="border-gradient-grey border-2 border-transparent rounded-lg block leading-6 px-2 w-48" name="voltage" type="number" defaultValue={node.config.sendDataDeltas.voltage} />
						</div>
					</div>
				</div>
				<button disabled={!changed} onClick={e => setChanged(false)} className={`${changed ? "border-gradient-primary" : "border-gradient-grey text-gray-500"} transition-color px-4 font-bold text-lg rounded-xl border-transparent border-2 block absolute right-8 bottom-4 `}>Save</button>
			</form>
		</div>
	);
}

export default NodeConfig;