import React, { useEffect, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { nodeService } from '../../services/nodes/nodeService';
import Icon from "../../assets/Icon";
import NodeConfig from "./components/NodeConfig";
import Map from "./components/Map";
import DatasinkConfig from "./components/DatasinkConfig";
import Notifications from "./components/Notifications";

import * as types from '../../types';


const NodeInfo = function() {

	const [node, setNode] = useState<types.Node | undefined>();
	const [searchParams, setSearchParams] = useSearchParams();
	const nodeID: string = searchParams.get("id");

	useEffect(() => {
		nodeService.getNodeById(nodeID).then(res => {
			res.text().then((text: string) => {
				setNode(JSON.parse(text));
			})
		})
	}, []);

	function updateNode(e, updatedNode: types.Node) {
		e.preventDefault();

		nodeService.updateNode(updatedNode);
		setNode({ ...updatedNode });

		return false;
	}


	if (!node)
		return (<div className="w-full h-full bg-white z-10 pageTransitionAnimated"></div>);

	return (
		<div className="w-full h-full bg-white z-10 pageTransitionAnimated">
			<div className="h-full mx-12">
				<div className="flex items-center h-14 pt-6">
					<Link to="/nodes" className="p-2 rounded-lg shadow-lg inline-block">
						<Icon
							name="arrow-down"
							className="rotate-90 inline text-primary h-4"
							size={20}
						/>
						<span className="font-bold text-transparent bg-clip-text bg-gradient-to-r from-primary to-primaryDark">Back to Nodes</span>
					</Link>
					<span className="text-4xl font-extrabold ml-16">{node.displayName}</span>
				</div>
				<div className="pt-10 grid auto-rows-min gap-8 grid-cols-2">
					<div className="col-span-2 flex h-96 gap-8">
						<NodeConfig node={node} updateNodeFunc={updateNode} />
						<Map />
					</div>
					<DatasinkConfig node={node} updateNodeFunc={updateNode} />
					<Notifications />
				</div>
			</div>
		</div>
	);
}

export default NodeInfo;
