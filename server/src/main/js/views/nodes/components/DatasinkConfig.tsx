import React, { useState, useEffect } from "react";
import MQTTSinkConfig from "./MQTTSinkConfig";

import Icon from "../../../assets/Icon";
import { datasinkService } from "../../../services/datasinks/datasinkService"
import * as types from "../../../types";
import config from "../../../configuration/config";

const datasinkIDMappings = config.datasinks.datasinkIDMappings;

const DatasinkConfig = function(props: { node: types.Node, updateNodeFunc: Function }) {

	const [datasinks, setDatasinks] = useState<types.datasink[]>();
	const [selectedSink, setSelectedSink] = useState<types.datasink | undefined>(undefined);
	const [selectionOpen, setSelectionOpen] = useState(false);

	useEffect(() => {
		datasinkService.getAllAvailable().then(res => {
			res.text().then((text: string) => {
				setDatasinks(JSON.parse(text));
			})
		})
	}, []);


	function DatasinkConfig(localProps: { datasink: types.datasink }) {
		switch (localProps.datasink?.sinkId) {
			case datasinkIDMappings.MQTT:
				return <MQTTSinkConfig key={localProps.datasink.id} sink={localProps.datasink} node={props.node} updateNodeFunc={props.updateNodeFunc} unselectSink={() => setSelectedSink(undefined)} />;

			default:
				return (
					<div className="h-full flex items-center justify-center">
						<span className="text-gray-600 italic">Select a datasink to configure it.</span>
					</div>
				);
		}
	}

	if (!datasinks)
		return <div />;


	return (
		<div className="rounded-xl shadow-xl h-96 flex flex-col">
			<div>
				<span className="block text-3xl font-bold ml-8 mt-2">Datasinks</span>
				<div
					className="mx-auto mt-4 w-8/12 relative"
				>
					<button
						type="button"
						aria-haspopup="listbox"
						aria-expanded={selectionOpen}
						className={`${selectionOpen ? "expanded" : ""} block text-xl font-bold w-full h-10 border-gradient-primary border-2 border-transparent rounded-lg`}
						onClick={e => setSelectionOpen(!selectionOpen)}
					>
						{selectedSink?.displayName}
						<div className="float-right mr-4 -mt-1">
							<Icon
								name="arrow-down"
								size={20}
								className={`align-middle inline ${selectionOpen ? "rotate-180" : ""} transform-transform duration-200`}
							/>
						</div>
					</button>
					<ul
						className={`${selectionOpen ? "block" : "hidden"} z-10 absolute w-full rounded-lg border-gray-600 border-2 border-t-0 rounded-t-none pt-1 top-9`}
						role="listbox"
						aria-activedescendant={selectedSink?.displayName}
						tabIndex={-1}
					>
						{datasinks.map(datasink => (
							<li
								key={datasink.id}
								role="option"
								aria-selected={selectedSink == datasink}
								tabIndex={0}
								className={`cursor-pointer text-center bg-background font-bold text-xl h-10 py-2 border-gray-300 border-b last:border-b-0 ${props.node.datasinksConfigs.filter(datasinkConfig => datasinkConfig.datasink.id == datasink.id).length == 0 ? "text-gray-400" : ""}`}
								onClick={e => {
									setSelectedSink(datasink);
									setSelectionOpen(false);
								}}
							>
								{datasink.displayName}
							</li>
						))
						}
					</ul>
				</div>
			</div>
			<div className="flex-grow">
				<DatasinkConfig datasink={selectedSink} />
			</div>
		</div>
	);
}

export default DatasinkConfig;