package renato.araujo.account.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import renato.araujo.account.manager.dto.ContaDTO;
import renato.araujo.account.manager.model.Conta;

@Mapper
public interface ContaMapper {

    ContaMapper INSTANCE = Mappers.getMapper(ContaMapper.class);

    ContaDTO dtoFromConta(Conta conta);

}
