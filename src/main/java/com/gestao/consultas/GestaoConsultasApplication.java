package com.gestao.consultas;

import com.gestao.consultas.cli.TerminalInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestaoConsultasApplication implements CommandLineRunner {

	@Autowired
	private TerminalInterface terminalInterface;

	public static void main(String[] args) {
		SpringApplication.run(GestaoConsultasApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		terminalInterface.iniciar();
	}
}