import React, { useState } from "react";

import * as types from "../../../types";
import config from "../../../configuration/config";

const datasinkIDMappings = config.datasinks.datasinkIDMappings;

const MQTTSinkConfig = function(props: { node: types.Node, updateNodeFunc: Function, sink: types.datasink, unselectSink: Function }) {

	function datasinkFromConfigList() {
		return props.node.datasinksConfigs.filter(datasinkConfig => datasinkConfig.datasink.sinkId == props.sink.sinkId);
	}

	const [authEnabled, setAuthEnabled] = useState(datasinkFromConfigList[0]?.datasinkConfig?.username != null);
	const [changed, setChanged] = useState(false);
	const [datasinkConfig, setDatasinkConfig] = useState<types.datasinkConfig>(
		datasinkFromConfigList()[0] || {
			id: null,
			datasink: props.sink,
			datasinkConfig: {},
		});


	function saveConfig(e) {
		e.preventDefault();

		datasinkConfig.datasinkConfig.host = e.target.elements.host.value;
		datasinkConfig.datasinkConfig.topic = e.target.elements.topic.value;
		if (e.target.elements.auth.checked) {
			datasinkConfig.datasinkConfig.username = e.target.elements.username.value;
			datasinkConfig.datasinkConfig.password = e.target.elements.password.value;
		} else {
			datasinkConfig.datasinkConfig.username = null;
			datasinkConfig.datasinkConfig.password = null;
		}

		if (datasinkConfig.id == null) {
			props.node.datasinksConfigs.push(datasinkConfig);
		} else {
			props.node.datasinksConfigs[props.node.datasinksConfigs.indexOf(datasinkConfig)] = datasinkConfig;
		}

		props.updateNodeFunc(e, props.node);

		return false;
	}

	function deleteSinkConfig(e) {
		e.preventDefault();

		props.node.datasinksConfigs.splice(props.node.datasinksConfigs.indexOf(datasinkConfig), 1);
		props.updateNodeFunc(e, props.node);

		props.unselectSink();

		return false;
	}

	return (
		<div className="relative h-full">
			<form onSubmit={saveConfig} className="ml-8 mt-6">
				<div className="flex">
					<div>
						<div>
							<label htmlFor="host" className="ml-1 text-base block text-gray-600">Host</label>
							<input onChange={e => setChanged(true)} className="border-gradient-primary border-2 border-transparent rounded-lg block leading-8 px-2" type="text" name="host" defaultValue={datasinkConfig.datasinkConfig.host} />
						</div>
						<div>
							<label htmlFor="topic" className="ml-1 text-base block text-gray-600">Topic</label>
							<input onChange={e => setChanged(true)} className="border-gradient-primary border-2 border-transparent rounded-lg block leading-8 px-2" type="text" name="topic" defaultValue={datasinkConfig.datasinkConfig.topic} />
						</div>
					</div>
					<div className="ml-8">
						<div className="flex items-center">
							<label htmlFor="auth" className="ml-1 text-base text-gray-600">Authentication</label>
							<input onChange={e => {setChanged(true); setAuthEnabled(e.target.checked)}} className="w-6 h-6 ml-8" type="checkbox" name="auth" defaultChecked={!!datasinkConfig.datasinkConfig.username} />
						</div>
						<div>
							<label htmlFor="username" className="ml-1 text-base block text-gray-600">Username</label>
							<input disabled={!authEnabled} onChange={e => setChanged(true)} className={`${authEnabled ? "border-gradient-primary" : "border-gradient-grey"} border-2 border-transparent rounded-lg block leading-8 px-2`} type="text" name="username" defaultValue={datasinkConfig.datasinkConfig.username} />
						</div>
						<div>
							<label htmlFor="password" className="ml-1 text-base block text-gray-600">Password</label>
							<input disabled={!authEnabled} onChange={e => setChanged(true)} className={`${authEnabled ? "border-gradient-primary" : "border-gradient-grey"} border-2 border-transparent rounded-lg block leading-8 px-2`} type="text" name="password" defaultValue={datasinkConfig.datasinkConfig.password} />
						</div>
					</div>
				</div>
				<div className="absolute right-8 bottom-12">
					<button type="button" className="mr-4 font-semibold text-lg rounded-xl border-transparent border-2 border-gradient-red px-4" onClick={deleteSinkConfig}>Delete configuration</button>
					<button disabled={!changed} onClick={e => setChanged(false)} className={`${changed ? "border-gradient-primary" : "border-gradient-grey text-gray-500"} transition-color px-4 font-bold text-lg rounded-xl border-transparent border-2`}>Save</button>
				</div>
			</form>
		</div>
	);
}


export default MQTTSinkConfig;