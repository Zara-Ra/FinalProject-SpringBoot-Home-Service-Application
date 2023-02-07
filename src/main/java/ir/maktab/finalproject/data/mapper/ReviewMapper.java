package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.ReviewDto;
import ir.maktab.finalproject.data.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(source = "orderId", target = "customerOrder.id")
    Review convertReview(ReviewDto reviewDto);

    @Mapping(source = "customerOrder.id",target = "orderId")
    ReviewDto convertReview(Review review);
}
