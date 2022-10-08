import config from '../../configuration/config';
import * as types from '../../types';

const nodeConfig = config.nodeService;


export const nodeService = {
	getAll,
	getNodeById,
	updateNode,
};

function getAll(): Promise<any> {
	return fetch(`${nodeConfig.api.path}/all`, {
		method: "GET",
	}).then(res => {
		return res;
	}, error => {
		return error;
	})
}

function getNodeById(id: string): Promise<any> {
	return fetch(`${nodeConfig.api.path}?id=${id}`, {
		method: "GET",
	}).then(res => {
		return res;
	}, error => {
		return error;
	})
}

function updateNode(node: types.Node) {
	fetch(`${nodeConfig.api.path}/update?id=${node.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(node),
    });
}