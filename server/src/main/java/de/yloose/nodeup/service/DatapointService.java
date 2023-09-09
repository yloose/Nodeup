package de.yloose.nodeup.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.yloose.nodeup.models.DatapointEntity;
import de.yloose.nodeup.models.NodeEntity;
import de.yloose.nodeup.networking.NodeData.DatapointIn;
import de.yloose.nodeup.repository.DatapointRepository;

@Service
public class DatapointService {
	
	@Autowired
	private DatapointRepository datapointRepository;
	
	@Value("${persistence.datapointsToSave:10}")
	private int datapointsToSave;
	
	public void saveDatapoints(List<DatapointIn> datapointsIn, NodeEntity node) {
		
		// Convert datapointsIn to datapoinEntities
		Set<DatapointEntity> datapoints = new HashSet<>();
		datapointsIn.forEach(dpIn -> datapoints.add(new DatapointEntity(dpIn, node)));
		
		// Save all datapointEntities
		datapoints.forEach(datapoint -> datapointRepository.save(datapoint));
		
		// Delete all datapoints over datapointsToSave
		List<DatapointEntity> allDatapoints = datapointRepository.findAllByNodeOrderByTimestampDesc(node);
		datapointRepository.deleteAll(allDatapoints.subList(datapointsToSave, allDatapoints.size()));
		
	}
}
