import React from 'react';
import Icon from '../../assets/Icon';

const SidebarItem = function({ data, clickCallback, activeName }) {

	return (
		<div
			className={`duration-300 transition-[border-color] border-background border-2 hover:border-primary pl-4 py-2 w-10/12 mb-2 flex items-center cursor-pointer rounded-xl ${activeName == data.name ? "bg-gradient-to-br from-primary to-primaryDark text-white" : ""}`}
			onClick={(e) => clickCallback(e, data.name)}	
		>
			<Icon
				name={data.icon}
				className="inline"
				size={25}
			/>
			<span className="ml-3 text-lg font-medium">
				{data.name}
			</span>
		</div>
	);
}

const SidebarSection = function({ data, clickCallback, activeName }) {


	return (
		<div
			className="mt-4 after:content-[''] after:w-3/4 after:h-1 after:block after:border-b after:border-gray-300 after:mx-auto"
		>
			<span className="ml-4 text-sm text-gray-500">
				{data.name}
			</span>
			<div className="ml-8 mt-4">
				{data.items.map(item => (
					<SidebarItem key={item.name} data={item} clickCallback={clickCallback} activeName={activeName} />
				))}
			</div>
		</div>
	);
}


export default SidebarSection;