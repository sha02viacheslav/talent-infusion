package com.talent.infusion.controller.main;

import java.util.HashMap;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class MainController {
    public static void getMain(Context ctx) {
        HashMap<String, String> resultMap = new HashMap<>();
        resultMap.put("message", "talentInfusionApi");
        ctx.status(HttpStatus.OK).json(resultMap);
    }
}
