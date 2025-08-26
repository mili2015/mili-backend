package br.com.mili.milibackend.fornecedor.application.service;

import br.com.mili.milibackend.fornecedor.application.dto.*;
import br.com.mili.milibackend.fornecedor.domain.entity.Fornecedor;
import br.com.mili.milibackend.fornecedor.domain.usecases.ValidatePermissionFornecedorUseCase;
import br.com.mili.milibackend.fornecedor.infra.dto.FornecedorResumoDto;
import br.com.mili.milibackend.fornecedor.infra.repository.fornecedorRepository.FornecedorRepository;
import br.com.mili.milibackend.shared.exception.types.NotFoundException;
import br.com.mili.milibackend.shared.page.pagination.MyPage;
import br.com.mili.milibackend.shared.page.pagination.MyPageable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Optional;

import static br.com.mili.milibackend.fornecedor.adapter.exception.FornecedorCodeException.FORNECEDOR_NAO_ENCONTRADO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

    @InjectMocks
    private FornecedorService fornecedorService;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ValidatePermissionFornecedorUseCase validatePermissionFornecedorUseCase;

    @Test
    void test_GetByCodUsuario__deveRetornarFornecedor_quandoEncontrado() {
        var input = new FornecedorGetByCodUsuarioInputDto();
        input.setCodUsuario(123);

        var entity = new Fornecedor();
        entity.setRazaoSocial("Teste");

        var dto = new FornecedorGetByCodUsuarioOutputDto();
        dto.setRazaoSocial("Teste");

        when(fornecedorRepository.findByCodUsuario(123)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, FornecedorGetByCodUsuarioOutputDto.class)).thenReturn(dto);

        var result = fornecedorService.getByCodUsuario(input);

        assertNotNull(result);
        assertEquals("Teste", result.getRazaoSocial());
    }

    @Test
    void test_GetByCodUsuario__deveRetornarNull_quandoNaoEncontrado() {
        var input = new FornecedorGetByCodUsuarioInputDto();
        input.setCodUsuario(999);

        when(fornecedorRepository.findByCodUsuario(999)).thenReturn(Optional.empty());

        var result = fornecedorService.getByCodUsuario(input);

        assertNull(result);
    }

    @Test
    void test_GetById__deveRetornarFornecedor_quandoEncontrado() {
        var id = 1;
        var entity = new Fornecedor();
        entity.setCodigo(id);
        entity.setRazaoSocial("Teste");

        var dto = new FornecedorGetByIdOutputDto();
        dto.setRazaoSocial("Teste");

        when(fornecedorRepository.findById(id)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, FornecedorGetByIdOutputDto.class)).thenReturn(dto);

        var result = fornecedorService.getById(id);

        assertNotNull(result);
        assertEquals("Teste", result.getRazaoSocial());
    }

    @Test
    void test_GetById__deveRetornarNull_quandoNaoEncontrado() {
        var id = 999;
        when(fornecedorRepository.findById(id)).thenReturn(Optional.empty());

        var result = fornecedorService.getById(id);

        assertNull(result);
    }

    @Test
    void test_UpdateMeusDados__deveAtualizarPorId_quandoExistir() {
        // Arrange
        var input = new FornecedorMeusDadosUpdateInputDto();
        input.setId(1);
        input.setCodUsuario(999);
        input.setContato("Contato");
        input.setEmail("email@test.com");
        input.setCelular("123456789");

        var entity = new Fornecedor();
        entity.setCodigo(1);
        entity.setRazaoSocial("Antiga");

        var outputDto = new FornecedorMeusDadosUpdateOutputDto();
        outputDto.setRazaoSocial("Antiga");

        when(fornecedorRepository.findById(1)).thenReturn(Optional.of(entity));

        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(true);

        when(fornecedorRepository.save(entity)).thenReturn(entity);
        when(modelMapper.map(entity, FornecedorMeusDadosUpdateOutputDto.class)).thenReturn(outputDto);

        // Act
        var result = fornecedorService.updateMeusDados(input);

        // Assert
        assertNotNull(result);
        assertEquals("Antiga", result.getRazaoSocial());
        verify(fornecedorRepository).save(entity);
        verify(modelMapper).map(entity, FornecedorMeusDadosUpdateOutputDto.class);
    }

    @Test
    void test_UpdateMeusDados__deveAtualizarPorCodUsuario_quandoExistir() {
        var input = new FornecedorMeusDadosUpdateInputDto();
        input.setCodUsuario(123);
        input.setContato("Contato");
        input.setEmail("email@test.com");
        input.setCelular("987654321");

        var entity = new Fornecedor();

        var outputDto = new FornecedorMeusDadosUpdateOutputDto();

        when(fornecedorRepository.findByCodUsuario(123)).thenReturn(Optional.of(entity));

        when(fornecedorRepository.save(entity)).thenReturn(entity);
        when(modelMapper.map(entity, FornecedorMeusDadosUpdateOutputDto.class)).thenReturn(outputDto);
        when(validatePermissionFornecedorUseCase.execute(any(), any())).thenReturn(true);

        var result = fornecedorService.updateMeusDados(input);

        assertNotNull(result);
        verify(fornecedorRepository).findByCodUsuario(123);
        verify(fornecedorRepository).save(entity);
    }

    @Test
    void test_UpdateMeusDados__deveLancarNotFound_quandoNaoExistir() {
        var input = new FornecedorMeusDadosUpdateInputDto();
        input.setId(999);
        input.setCodUsuario(999);
        input.setContato("Contato");
        input.setEmail("email@test.com");
        input.setCelular("000000000");

        when(fornecedorRepository.findById(999)).thenReturn(Optional.empty());

        var ex = assertThrows(NotFoundException.class, () -> fornecedorService.updateMeusDados(input));
        assertEquals(FORNECEDOR_NAO_ENCONTRADO.getMensagem(), ex.getMessage());
        assertEquals(FORNECEDOR_NAO_ENCONTRADO.getCode(), ex.getCode());
    }

    @Test
    void test_GetAll__deveRetornarPagina_quandoSemFiltros() {
        var input = new FornecedorGetAllInputDto();
        input.setPageable(new MyPageable(1, 10));

        var entity = new FornecedorResumoDto();
        entity.setCodigo(1);
        entity.setRazaoSocial("Teste");

        var page = new PageImpl<>(Collections.singletonList(entity), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "codigo")), 1);
        var dto = new FornecedorGetAllOutputDto();
        dto.setRazaoSocial("Teste");

        when(fornecedorRepository.getAll(any(), eq(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "codigo"))))).thenReturn(page);
        when(modelMapper.map(entity, FornecedorGetAllOutputDto.class)).thenReturn(dto);

        MyPage<FornecedorGetAllOutputDto> result = fornecedorService.getAll(input);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Teste", result.getContent().get(0).getRazaoSocial());
        assertEquals(1, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(1, result.getTotalElements());
    }

    @Test
    void test_GetAll__deveRetornarPaginaFiltrada_quandoComFiltros() {
        var input = new FornecedorGetAllInputDto();
        input.setCodigo(1);
        input.setRazaoSocial("Teste");
        input.setCgcCpf("123456789");
        input.setPageable(new MyPageable(1, 10));

        var entity = new FornecedorResumoDto();
        entity.setCodigo(1);
        entity.setRazaoSocial("Teste");
        entity.setCgcCpf("123456789");

        var page = new PageImpl<>(Collections.singletonList(entity), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "codigo")), 1);
        var dto = new FornecedorGetAllOutputDto();
        dto.setRazaoSocial("Teste");

        when(fornecedorRepository.getAll(any(), eq(PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "codigo"))))).thenReturn(page);
        when(modelMapper.map(entity, FornecedorGetAllOutputDto.class)).thenReturn(dto);

        MyPage<FornecedorGetAllOutputDto> result = fornecedorService.getAll(input);


        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Teste", result.getContent().get(0).getRazaoSocial());
    }
}