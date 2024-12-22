package org.mojimoon.planner.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.mojimoon.planner.model.ScenicSpot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataLoader_S extends DataLoader<ScenicSpot> {
    private String filePath;
    private InputStream stream;

    public DataLoader_S() {
        filePath = "data/scenicspots.json";
    }

    public List<ScenicSpot> loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ScenicSpot> scenicSpots = null;

        try (InputStream inputStream = (stream != null) ? stream : DataLoader_S.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.err.println("file not found: " + filePath);
                return null;
            }

            scenicSpots = objectMapper.readValue(inputStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ScenicSpot.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return scenicSpots;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
	public void setInputStream(InputStream stream) {
    	this.filePath = "InputStream";
		this.stream = stream;
	}
}