package renato.araujo.account.manager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import renato.araujo.account.manager.dto.TransacaoDTO;
import renato.araujo.account.manager.model.Transacao;

import java.util.List;

@Mapper
public interface TransacaoMapper {

    TransacaoMapper INSTANCE = Mappers.getMapper(TransacaoMapper.class);

    List<TransacaoDTO> fromTransacaoList(List<Transacao> transacoes);

}
