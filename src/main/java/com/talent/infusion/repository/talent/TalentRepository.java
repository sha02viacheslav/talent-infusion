package com.talent.infusion.repository.talent;

import dev.fuxing.airtable.AirtableApi;
import dev.fuxing.airtable.AirtableTable;
import io.github.cdimascio.dotenv.Dotenv;

public class TalentRepository {
    static Dotenv dotenv = Dotenv.configure().load();
    static String apiKey = dotenv.get("AIRTABLE_APIKEY");
    static String baseId = dotenv.get("AIRTABLE_BASE_ID");
    AirtableApi api = new AirtableApi(apiKey);
    AirtableTable table = api.base(baseId).table("Resume Database");


    public TalentRepository() {
    }

    public AirtableTable.PaginationList search(String name, String areaOfWork, String title, int limit, int skip) {
        AirtableTable.PaginationList list = table.list(querySpec -> {
            // Pagination
            querySpec.pageSize(limit);

            querySpec.filterByFormula(String.format("SEARCH('%s', {name})", name));
        });
        return list;
    }

    public long countQuery(String filterByFormula) {
        return table.list(querySpec -> {
            querySpec.filterByFormula(filterByFormula);
        }).stream().count();
    }
}
