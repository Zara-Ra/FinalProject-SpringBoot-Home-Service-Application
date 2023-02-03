package ir.maktab.finalproject.controller.enums;

import ir.maktab.finalproject.data.entity.ExpertOffer;
import ir.maktab.finalproject.util.sort.SortExpertOffer;

import java.util.Comparator;

public enum SortType {
    PRICE_ACS(SortExpertOffer.SortByPriceAcs),
    PRICE_DSC(SortExpertOffer.SortByPriceDsc),
    SCORE_ACS(SortExpertOffer.SortByScoreAcs),
    SCORE_DSC(SortExpertOffer.SortByScoreDsc);

    public Comparator<ExpertOffer> getComparator() {
        return comparator;
    }

    private final Comparator<ExpertOffer> comparator;

    SortType(Comparator<ExpertOffer> comparator) {
        this.comparator = comparator;
    }
}
