import React from 'react';
import { Link } from 'react-router-dom';
import Icon from '../../assets/Icon';
import NodeBody from './components/NodeBody';

import * as types from '../../types';

interface NodeProps {
	node: types.Node;
	active: boolean;
	customClickEvent: Function

}

class Node extends React.Component<NodeProps> {

	constructor(props: NodeProps) {
		super(props);
		
		this.props.node.shortData = {battery: {batteryPercentage: 89, voltage: 0, remainingTime: 0}, latestDataSet: {timestamp: 0, temperature: 22.91, humidity: 53, pressure: 1003.73, voltage: 0}, firstRegistered: 0};
	}


	render() {
		return (
			<div
				className={`shadow-lg rounded-lg mb-6 w-full transition-[height] ease duration-300 ${this.props.active ? "h-72" : "h-20"}`}
				onClick={e => this.props.customClickEvent(e, this.props.node.id)}
			>
				<div
					className="h-20 w-full flex items-center"
				>
					<img
						src="/images/espressif.svg"
						className="ml-4 h-16"
					/>
					<div
						className="ml-4"
					>
						<span
							className="font-bold text-xl block"
						>
							{this.props.node.displayName}
						</span>
						<img
							src="/images/location.svg"
							className="h-4 inline-block"
							style={{ filter: "invert(70%) sepia(12%) saturate(144%) hue-rotate(165deg) brightness(90%) contrast(91%)" }}
						/>
						<span
							className="relative text-m text-gray-800 ml-1 top-[0.1rem]"
						>
							Wohnzimmer
						</span>
					</div>
					<div className="ml-auto mr-4 flex items-center">
						<div className="block mr-8 rounded-3xl px-3 py-1.5 bg-gradient-to-br from-primary to-primaryDark flex items-center text-white">
							<span
								className="mr-2 font-bold"
							>
								{this.props.node.shortData.battery.batteryPercentage}%
							</span>
							<Icon
								name={this.props.node.shortData.battery.batteryPercentage < 66 ? this.props.node.shortData.battery.batteryPercentage < 33 ? "battery-empty-line" : "battery-half-line" : "battery-full-line"}
								size={25}
							/>
						</div>
						<Icon
							name="arrow-down"
							className={`block text-primary h-4  transition-transform ${this.props.active ? "rotate-180" : ""}`}
							size={30}
						/>

					</div>
				</div>
				<div
					className={`w-full before:content-[''] before:w-[90%] before:border-t before:mx-auto before:block before:border-gray-300 ${this.props.active ? "initial" : "hidden"}`}
				>
					<NodeBody node={this.props.node}/>
				</div>
			</div>
		)
	}
}


export default Node;