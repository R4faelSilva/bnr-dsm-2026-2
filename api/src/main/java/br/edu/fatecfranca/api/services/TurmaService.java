package br.edu.fatecfranca.api.services;


import java.util.List;
import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import br.edu.fatecfranca.api.dtos.TurmaResponse;


import br.edu.fatecfranca.api.models.Curso;
import br.edu.fatecfranca.api.models.Instrutor;
import br.edu.fatecfranca.api.models.Turma;
import br.edu.fatecfranca.api.repositories.CursoRepository;
import br.edu.fatecfranca.api.repositories.InstrutorRepository;
import br.edu.fatecfranca.api.repositories.MatriculaRepository;
import br.edu.fatecfranca.api.repositories.TurmaRepository;


@Service
public class TurmaService {


   private final TurmaRepository repository;
   private final MatriculaRepository matriculaRepository;
   private final CursoRepository cursoRepository;
   private final InstrutorRepository instrutorRepository;
   
   public TurmaService(
           TurmaRepository repository,
           CursoRepository cursoRepository,
           InstrutorRepository instrutorRepository,
           MatriculaRepository matriculaRepository
       ) {
       this.repository = repository;
       this.cursoRepository = cursoRepository;
       this.instrutorRepository = instrutorRepository;
       this.matriculaRepository = matriculaRepository;
   }


   // Este método monta uma resposta para o GET de /turmas
   // incluindo os dados de curso e instrutor
   private TurmaResponse montarResposta(Turma turma) {
       Curso curso = cursoRepository.findById(turma.getCursoId())
               .orElse(null);


       Instrutor instrutor = turma.getInstrutorId() == null
               || turma.getInstrutorId().isBlank()
               ? null
               : instrutorRepository.findById(turma.getInstrutorId())
                       .orElse(null);


       return new TurmaResponse(
               turma.getId(),
               turma.getCursoId(),
               turma.getInstrutorId(),
               turma.getDataFim(),
               turma.getDataFim(),
               turma.getNumVagas(),
               turma.getHorario(),
               curso,
               instrutor);
   }


   public List<TurmaResponse> listarComDetalhes() {
       return repository.findAll().stream()
               .map(this::montarResposta)
               .toList();
   }


   public Optional<TurmaResponse> buscarComDetalhes(String id) {
       return repository.findById(id)
               .map(this::montarResposta);
   }


   public List<TurmaResponse> listarPorCursoComDetalhes(String cursoId) {
        return listarPorCurso(cursoId).stream()
               .map(this::montarResposta)
               .toList();
   }

   private void verificarCurso(String cursoId) {
        if (cursoId == null || cursoId.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                "O identificador curso é obrigatório")   ;
        }

        if (!cursoRepository.existsById(cursoId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                "Cursonão encontrado.");
        }
   }

   private void verificarInstrutor(String instrutorId) {
        if (instrutorId == null || instrutorId.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                "O identificador instrutor é obrigatório");
        }

        if (!instrutorRepository.existsById(instrutorId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Istrutor não encontrado");
        }
   }

    public List<Turma> listarTodos() {
        return repository.findAll();
    }

    public Optional<Turma> buscarPorId(String id) {
        return repository.findById(id);
    }

    public List<Turma> listarPorCurso(String cursoId) {
        verificarCurso(cursoId);
        return repository.findByCursoId(cursoId);
    }

    public List<Turma> listarPorInstrutor(String instrutorId) {
        verificarInstrutor(instrutorId);
        return repository.findByInstrutorId(instrutorId);
    }

    public Turma salvar(Turma turma) {
        verificarCurso(turma.getCursoId());
        verificarInstrutor(turma.getInstrutorId());
        return repository.save(turma);
    }

    public void excluir(String id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                "Turma não encontrada.");
        }

        if (matriculaRepository.existsByTurmaId(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                "Não é possivel excluir uma turma com matriculas vinculadas.");
        }

        repository.deleteById(id);
    }
}
