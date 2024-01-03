package com.talent.infusion.service.talent;

import com.talent.infusion.repository.talent.TalentRepository;
import dev.fuxing.airtable.AirtableTable;

import javax.inject.Singleton;

@Singleton
public class TalentService {
    private final TalentRepository talentRepository;

    public TalentService(TalentRepository talentRepository) {
        this.talentRepository = talentRepository;

    }

    public AirtableTable.PaginationList search(String name, String areaOfWork, String title, int limit, int skip){
        return talentRepository.search(name, areaOfWork, title, limit, skip);
    }

    public long countQuery(String filterByFormula){
        return talentRepository.countQuery(filterByFormula);
    }
}
