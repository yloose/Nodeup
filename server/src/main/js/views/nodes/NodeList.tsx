import React from 'react';
import Node from './Node';
import { nodeService } from "../../services/nodes/nodeService";

import * as types from '../../types';

type NodeListState = {
	nodeList: types.Node[];
	activeID: string;
}

class NodeView extends React.Component<{}, NodeListState> {

	constructor(props: NodeListState) {
		super(props);
		this.state = { nodeList: [], activeID: null };
	}

	componentDidMount(): void {
		nodeService.getAll().then(res => {
			res.text().then((text: string) => {
				this.setState({
					nodeList: JSON.parse(text)
				});
			})
		})
	}

	handleNodeClick(e, id: string) {
		if (e.defaultPrevented)
			return;
			
		this.setState(state => {
			return { activeID: state.activeID == id ? null : id };
		})
	}

	render() {
		return (
			<div className="w-[1000px] ml-36 pt-20 pageTransitionNotAnimated">
				{this.state.nodeList.map((node: types.Node) => (
					<Node
						key={node.id}
						node={node}
						active={this.state.activeID == node.id ? true : false}
						customClickEvent={this.handleNodeClick.bind(this)} />
				))}
			</div>
		);
	}
}



export default NodeView;