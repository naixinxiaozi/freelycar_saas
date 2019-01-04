package com.freelycar.saas.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author tangwei - Toby
 * @date 2019-01-03
 * @email toby911115@gmail.com
 */
public class CarBrandJSONResolver {

    public static String readCarJSON() {
        String jsonStr;
        try {
            Resource resource = new ClassPathResource("static/carData.json");
            File jsonFile = resource.getFile();
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
}
