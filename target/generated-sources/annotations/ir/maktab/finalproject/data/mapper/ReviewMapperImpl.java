package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.ReviewDto;
import ir.maktab.finalproject.data.entity.CustomerOrder;
import ir.maktab.finalproject.data.entity.Review;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-14T19:32:24+0330",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public Review convertReview(ReviewDto reviewDto) {
        if ( reviewDto == null ) {
            return null;
        }

        Review review = new Review();

        review.setCustomerOrder( reviewDtoToCustomerOrder( reviewDto ) );
        review.setScore( reviewDto.getScore() );
        review.setComment( reviewDto.getComment() );

        return review;
    }

    @Override
    public ReviewDto convertReview(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDto reviewDto = new ReviewDto();

        reviewDto.setOrderId( reviewCustomerOrderId( review ) );
        reviewDto.setScore( review.getScore() );
        reviewDto.setComment( review.getComment() );

        return reviewDto;
    }

    protected CustomerOrder reviewDtoToCustomerOrder(ReviewDto reviewDto) {
        if ( reviewDto == null ) {
            return null;
        }

        CustomerOrder.CustomerOrderBuilder customerOrder = CustomerOrder.builder();

        customerOrder.id( reviewDto.getOrderId() );

        return customerOrder.build();
    }

    private Integer reviewCustomerOrderId(Review review) {
        if ( review == null ) {
            return null;
        }
        CustomerOrder customerOrder = review.getCustomerOrder();
        if ( customerOrder == null ) {
            return null;
        }
        Integer id = customerOrder.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
