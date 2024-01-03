package com.talent.infusion.controller.talent;

import com.talent.infusion.dto.TalentsQueryDto;
import com.talent.infusion.service.talent.TalentService;
import dev.fuxing.airtable.AirtableTable;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import io.javalin.json.JavalinJackson;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class TalentController {
    public TalentService talentService;

    public TalentController(TalentService talentService) {
        this.talentService = talentService;
    }

    public Handler search = ctx -> {
        String name = ctx.queryParam("name");
        String areaOfWork = ctx.queryParam("area_of_work");
        String title = ctx.queryParam("title");
        String limit = ctx.queryParam("limit");
        String skip = ctx.queryParam("skip");
        HashMap<String, Object> resultMap = new HashMap<>();

        try {
            AirtableTable.PaginationList talents = talentService.search(name, areaOfWork, title, limit != null ? Integer.parseInt(limit) : 10, skip != null ? Integer.parseInt(skip) : 0);

            resultMap.put("success", true);
            resultMap.put("data", talents);
            ctx.status(HttpStatus.OK).json(resultMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(resultMap);
        }
    };

    public Handler countQuery = ctx -> {
        JavalinJackson jackson = new JavalinJackson();
        HashMap<String, Object> resultMap = new HashMap<>();
        TalentsQueryDto talentsQueryDto;

        try {
            talentsQueryDto = jackson.fromJsonString(ctx.body(), TalentsQueryDto.class);
            long talents = talentService.countQuery(talentsQueryDto.getFilterByFormula());

            resultMap.put("success", true);
            resultMap.put("data", talents);
            ctx.status(HttpStatus.OK).json(resultMap);
        } catch (Exception e) {
            log.error(e.getMessage());
            resultMap.put("success", false);
            resultMap.put("message", e.getMessage());
            ctx.status(HttpStatus.SERVICE_UNAVAILABLE).json(resultMap);
        }
    };

}
