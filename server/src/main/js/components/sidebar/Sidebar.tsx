import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import SidebarSection from './SidebarSection';


let sidebarItems = {
	sections: [
		{
			name: "Main Tools",
			items: [
				{
					name: "Node Management",
					icon: "category",
					ref: "/nodes",
				},
				{
					name: "Weather Data",
					icon: "graph",
					ref: "/weatherdata",
				},
			],
		},
		{
			name: "Settings",
			items: [
				{
					name: "Node Settings",
					icon: "settings-line",
					ref: "/settings/nodes",
				},
				{
					name: "Data Settings",
					icon: "data-management",
					ref: "/settings/data",
				},
			],
		},
	]
}

const Sidebar = function(props) {
	
	let location = useLocation();
	let active;
	sidebarItems.sections.forEach(section => {
		let filtered = section.items.filter(item => location.pathname.includes(item.ref));
		if (filtered.length != 0)
			active = filtered[0].name;
	})
	
	const [activeName, setActiveName] = useState(active);
	
	function clickCallback(e, name) {
		setActiveName(name);
	}

	return (
		<div
			className="w-80 h-screen fixed border-r border-gray-300 z-50 bg-background dark:bg-dm_background"
		>
			<div className="mt-20">
				{sidebarItems.sections.map(section => (
					<SidebarSection key={section.name} data={section} clickCallback={clickCallback} activeName={activeName}/>
				))}
			</div>
		</div>
	);
}



export default Sidebar;