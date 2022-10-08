import React from 'react';
import { Link } from 'react-router-dom';

import * as types from '../../../types';

const NodeBody = function({node}) {
	
	
	return (
		<div className="w-full relative flex items-center">
			<div className="ml-12 mt-4">
				<div>
					<span className="text-2xl font-bold">Latest Data</span>
				</div>
				<div className="mt-1 relative before:absolute before:top-0 before:bottom-0 before:float-left before:inline-block before:w-1 before:rounded-xl before:content-[''] before:bg-gradient-to-b before:from-primary before:to-primaryDark">
					<span className="ml-4 block text-gray-700 text-sm leading-3">Temperature</span>
					<span className="ml-4 text-xl leading-6 font-bold">24.91°C</span>
					<span className="ml-4 mt-2 block text-gray-700 text-sm leading-3">Humidity</span>
					<span className="ml-4 text-xl leading-6 font-bold">57.18%</span>
					<span className="ml-4 mt-2 block text-gray-700 text-sm leading-3">Pressure</span>
					<span className="ml-4 text-xl leading-6 font-bold">998.47mBar</span>
				</div>
			</div>
			
			<div
				className="absolute bottom-0.5 right-2"
				onClick={e => e.preventDefault()}
			>
				<Link to={`/nodes/info?id=${node.id}`}>View more & Configure</Link>
			</div>
		</div>
	);
}

export default NodeBody;