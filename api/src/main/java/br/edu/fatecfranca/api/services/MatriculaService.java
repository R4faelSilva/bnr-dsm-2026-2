package br.edu.fatecfranca.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.dtos.MatriculaResponse;
import br.edu.fatecfranca.api.models.Aluno;
import br.edu.fatecfranca.api.models.Matricula;
import br.edu.fatecfranca.api.models.Turma;
import br.edu.fatecfranca.api.repositories.AlunoRepository;
import br.edu.fatecfranca.api.repositories.MatriculaRepository;
import br.edu.fatecfranca.api.repositories.TurmaRepository;

@Service
public class MatriculaService {

    private final MatriculaRepository repository;
    private final AlunoRepository alunoRepository;
    private final TurmaRepository turmaRepository;

    public MatriculaService(MatriculaRepository repository, AlunoRepository alunoRepository,
            TurmaRepository turmaRepository, TurmaService turmaService) {
        this.repository = repository;
        this.alunoRepository = alunoRepository;
        this.turmaRepository = turmaRepository;
    }

    private MatriculaResponse montarResposta(Matricula matricula) {
        Turma turma = turmaRepository.findById(matricula.getTurmaId())
                .orElse(null);

        Aluno aluno = matricula.getAlunoId() == null
                || matricula.getTurmaId().isBlank()
                        ? null
                        : alunoRepository.findById(matricula.getAlunoId())
                                .orElse(null);

        return new MatriculaResponse(
                matricula.getId(),
                matricula.getAlunoId(),
                matricula.getTurmaId(),
                matricula.getStatus(),
                matricula.getNotas(),
                aluno,
                turma);

    }

    public List<MatriculaResponse> listarComDetalhes() {
        return repository.findAll().stream()
                .map(this::montarResposta)
                .toList();
    }

    public Optional<MatriculaResponse> buscarComDetalhes(String id) {
        return repository.findById(id)
                .map(this::montarResposta);
    }

    public List<MatriculaResponse> listarPorTurmaComDetalhes(String turmaId) {
        return listarPorTurma(turmaId).stream()
                .map(this::montarResposta)
                .toList();
    }

    public List<MatriculaResponse> listarPorAlunoComDetalhes(String alunoId) {
        return listarPorAluno(alunoId).stream()
                .map(this::montarResposta)
                .toList();
    }

    private void verificarAluno(String alunoId) {
        if (alunoId == null || alunoId.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O identificador do aluno é obrigatório.");
        }

        if (!alunoRepository.existsById(alunoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Aluno não encontrado.");
        }
    }

    private void verificarTurma(String turmaId) {
        if (turmaId == null || turmaId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "O identificador Turma é obrigatório");
        }

        if (!turmaRepository.existsById(turmaId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Turma não encontrada.");
        }
    }

    public List<Matricula> listarTodos() {
        return repository.findAll();
    }

    public Optional<Matricula> buscarPorId(String id) {
        return repository.findById(id);
    }

    public List<Matricula> listarPorAluno(String alunoId) {
        verificarAluno(alunoId);
        return repository.findByAlunoId(alunoId);
    }

    public List<Matricula> listarPorTurma(String turmaId) {
        verificarTurma(turmaId);
        return repository.findByTurmaId(turmaId);
    }

    public Matricula salvar(Matricula matricula) {
        verificarAluno(matricula.getAlunoId());
        verificarTurma(matricula.getTurmaId());

        if (repository.existsByAlunoIdAndTurmaId(matricula.getAlunoId(), matricula.getTurmaId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O aluno já possui matrícula nesta turma.");
        }

        Turma turma = turmaRepository.findById(matricula.getTurmaId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, 
                    "Turma não encontrada."));

        long matriculasAtivas = repository.countByTurmaId(matricula.getTurmaId());

        if (matriculasAtivas >= turma.getNumVagas()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A turma está lotada. Não há vagas disponíveis.");
        }
        return repository.save(matricula);
    }

    public void excluir(String id) {
        repository.deleteById(id);
    }
}
