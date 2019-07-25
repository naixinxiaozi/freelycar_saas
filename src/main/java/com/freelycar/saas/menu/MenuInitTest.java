package com.freelycar.saas.menu;

import com.alibaba.fastjson.JSONArray;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author tangwei - Toby
 * @date 2019-05-16
 * @email toby911115@gmail.com
 */
public class MenuInitTest {
    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();

        File file;
        try (
                FileReader fileReader = new FileReader(file = ResourceUtils.getFile("classpath:init/router.json"));
                BufferedReader br = new BufferedReader(fileReader)
        ) {
            if (file.exists() && file.isFile()) {
                String s;
                while ((s = br.readLine()) != null) {
                    stringBuilder.append(s);
                    stringBuilder.append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = JSONArray.parseArray(stringBuilder.toString());
        System.out.println(jsonArray);
    }
}
