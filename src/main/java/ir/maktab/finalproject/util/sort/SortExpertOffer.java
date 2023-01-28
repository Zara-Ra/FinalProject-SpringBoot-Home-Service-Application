package ir.maktab.finalproject.util.sort;

import ir.maktab.finalproject.data.entity.ExpertOffer;

import java.util.Comparator;

public class SortExpertOffer {
    public static Comparator<ExpertOffer> SortByScoreAcs = (e1, e2) -> Double.compare(e1.getExpert().getAverageScore(), e2.getExpert().getAverageScore());
    public static Comparator<ExpertOffer> SortByScoreDsc = (e1, e2) -> Double.compare(e2.getExpert().getAverageScore(), e1.getExpert().getAverageScore());
    public static Comparator<ExpertOffer> SortByPriceAcs = (e1, e2) -> Double.compare(e1.getPrice(), e2.getPrice());
    public static Comparator<ExpertOffer> SortByPriceDsc = (e1, e2) -> Double.compare(e2.getPrice(), e1.getPrice());
}
