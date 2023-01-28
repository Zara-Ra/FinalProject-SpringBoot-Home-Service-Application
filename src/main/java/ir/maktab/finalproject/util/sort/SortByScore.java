package ir.maktab.finalproject.util.sort;

import ir.maktab.finalproject.data.entity.ExpertOffer;

import java.util.Comparator;

public class SortByScore implements Comparator<ExpertOffer> {
    @Override
    public int compare(ExpertOffer e1, ExpertOffer e2) {
        return (int) (e2.getExpert().getAverageScore() - e1.getExpert().getAverageScore());
    }
}
