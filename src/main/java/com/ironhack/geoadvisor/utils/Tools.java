package com.ironhack.geoadvisor.utils;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Tools {
    public static void sleep(int s){
        try {
            Thread.sleep(s);
        } catch (InterruptedException ignored) {}
    }

    public static String exportTableToJson(List<Object> list, String fileName) throws Exception {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File dir = new File("exports/"+ date);
        if (!dir.exists()) dir.mkdirs();
        var file = new File("exports/%s/%s".formatted(date, fileName));
        var deleted = !file.exists() || file.delete();

        if (!deleted) throw new Exception("Could not overwrite file");

        var gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String jsonList = gson.toJson(list);

        var writer = new FileWriter(file.getPath(), false);

        writer.write(jsonList);
        writer.close();

        return file.getPath();
    }
}
