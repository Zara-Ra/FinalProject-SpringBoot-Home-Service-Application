package ir.maktab.finalproject.data.mapper;

import ir.maktab.finalproject.data.dto.BaseServiceDto;
import ir.maktab.finalproject.data.dto.SubServiceDto;
import ir.maktab.finalproject.data.entity.services.BaseService;
import ir.maktab.finalproject.data.entity.services.SubService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-22T10:53:26+0330",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.4 (Oracle Corporation)"
)
public class ServiceMapperImpl implements ServiceMapper {

    @Override
    public BaseService convertBaseService(BaseServiceDto baseServiceDto) {
        if ( baseServiceDto == null ) {
            return null;
        }

        BaseService.BaseServiceBuilder baseService = BaseService.builder();

        baseService.id( baseServiceDto.getId() );
        baseService.baseName( baseServiceDto.getBaseName() );

        return baseService.build();
    }

    @Override
    public BaseServiceDto convertBaseService(BaseService baseService) {
        if ( baseService == null ) {
            return null;
        }

        BaseServiceDto baseServiceDto = new BaseServiceDto();

        baseServiceDto.setId( baseService.getId() );
        baseServiceDto.setBaseName( baseService.getBaseName() );

        return baseServiceDto;
    }

    @Override
    public List<BaseServiceDto> convertBaseServiceList(List<BaseService> list) {
        if ( list == null ) {
            return null;
        }

        List<BaseServiceDto> list1 = new ArrayList<BaseServiceDto>( list.size() );
        for ( BaseService baseService : list ) {
            list1.add( convertBaseService( baseService ) );
        }

        return list1;
    }

    @Override
    public SubService convertSubService(SubServiceDto subServiceDto) {
        if ( subServiceDto == null ) {
            return null;
        }

        SubService.SubServiceBuilder subService = SubService.builder();

        subService.baseService( subServiceDtoToBaseService( subServiceDto ) );
        subService.id( subServiceDto.getId() );
        subService.subName( subServiceDto.getSubName() );
        subService.basePrice( subServiceDto.getBasePrice() );
        subService.description( subServiceDto.getDescription() );

        return subService.build();
    }

    @Override
    public SubServiceDto convertSubService(SubService subService) {
        if ( subService == null ) {
            return null;
        }

        SubServiceDto subServiceDto = new SubServiceDto();

        subServiceDto.setBaseServiceName( subServiceBaseServiceBaseName( subService ) );
        subServiceDto.setId( subService.getId() );
        subServiceDto.setSubName( subService.getSubName() );
        subServiceDto.setBasePrice( subService.getBasePrice() );
        subServiceDto.setDescription( subService.getDescription() );

        return subServiceDto;
    }

    @Override
    public List<SubServiceDto> convertSubServiceList(List<SubService> list) {
        if ( list == null ) {
            return null;
        }

        List<SubServiceDto> list1 = new ArrayList<SubServiceDto>( list.size() );
        for ( SubService subService : list ) {
            list1.add( convertSubService( subService ) );
        }

        return list1;
    }

    protected BaseService subServiceDtoToBaseService(SubServiceDto subServiceDto) {
        if ( subServiceDto == null ) {
            return null;
        }

        BaseService.BaseServiceBuilder baseService = BaseService.builder();

        baseService.baseName( subServiceDto.getBaseServiceName() );

        return baseService.build();
    }

    private String subServiceBaseServiceBaseName(SubService subService) {
        if ( subService == null ) {
            return null;
        }
        BaseService baseService = subService.getBaseService();
        if ( baseService == null ) {
            return null;
        }
        String baseName = baseService.getBaseName();
        if ( baseName == null ) {
            return null;
        }
        return baseName;
    }
}
