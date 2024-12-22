package org.mojimoon.planner.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.mojimoon.planner.model.Plaza;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class DataLoader_P extends DataLoader<Plaza> {
    private String filePath;
    private InputStream stream;

    public DataLoader_P() {
        filePath = "data/shopping.json";
    }

    public List<Plaza> loadData() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Plaza> plazas = null;

        try (InputStream inputStream = (stream != null) ? stream : DataLoader_P.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.err.println("file not found: " + filePath);
                return null;
            }

            plazas = objectMapper.readValue(inputStream,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Plaza.class));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return plazas;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setInputStream(InputStream stream) {
    	this.filePath = "InputStream";
        this.stream = stream;
    }
}