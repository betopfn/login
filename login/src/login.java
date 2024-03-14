import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.util.Date;

public class login{

    private static ObjectContainer db;

    public static void main(String[] args) {
        abrirConexao();

        // Inserir Pessoa
        inserirPessoa(new Pessoa("João", "123.456.789-00", new Date(1990, 5, 15)));
        inserirPessoa(new Pessoa("Maria", "987.654.321-00", new Date(1985, 8, 20)));

        // Listar todas as pessoas
        listarPessoas();

        // Atualizar uma Pessoa
        Pessoa pessoaParaAtualizar = buscarPessoaPorCPF("123.456.789-00");
        if (pessoaParaAtualizar != null) {
            pessoaParaAtualizar.setName("João Silva");
            pessoaParaAtualizar.setDataNascimento(new Date(1990, 5, 16));
            atualizarPessoa(pessoaParaAtualizar);
        }

        // Listar todas as pessoas após a atualização
        listarPessoas();

        // Excluir uma Pessoa
        excluirPessoa("987.654.321-00");

        // Listar todas as pessoas após a exclusão
        listarPessoas();

        fecharConexao();
    }

    private static void abrirConexao() {
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "pessoas.db4o");
    }

    private static void fecharConexao() {
        db.close();
    }

    private static void inserirPessoa(Pessoa pessoa) {
        db.store(pessoa);
        System.out.println("Pessoa inserida: " + pessoa);
    }

    private static void atualizarPessoa(Pessoa pessoa) {
        db.store(pessoa);
        System.out.println("Pessoa atualizada: " + pessoa);
    }

    private static void excluirPessoa(String cpf) {
        Pessoa pessoaParaExcluir = new Pessoa();
        pessoaParaExcluir.setCpf(cpf);
        ObjectSet<Pessoa> result = db.queryByExample(pessoaParaExcluir);
        if (!result.isEmpty()) {
            Pessoa pessoaExcluida = result.next();
            db.delete(pessoaExcluida);
            System.out.println("Pessoa excluída: " + pessoaExcluida);
        } else {
            System.out.println("Pessoa não encontrada para exclusão com CPF: " + cpf);
        }
    }

    private static Pessoa buscarPessoaPorCPF(String cpf) {
        Pessoa exemplo = new Pessoa();
        exemplo.setCpf(cpf);
        ObjectSet<Pessoa> result = db.queryByExample(exemplo);
        if (!result.isEmpty()) {
            return result.next();
        }
        System.out.println("Pessoa não encontrada com CPF: " + cpf);
        return null;
    }

    private static void listarPessoas() {
        ObjectSet<Pessoa> result = db.queryByExample(Pessoa.class);
        System.out.println("Lista de Pessoas:");
        while (result.hasNext()) {
            System.out.println(result.next());
        }
        System.out.println("---------------------------");
    }
}

class Pessoa {
    private String nome;
    private String cpf;
    private Date dataNascimento;

    public Pessoa() {
    }

    public Pessoa(String nome, String cpf, Date dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters

    @Override
    public String toString() {
        return "Pessoa{" +
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", dataNascimento=" + dataNascimento +
                '}';
    }
}