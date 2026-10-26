import { describe, expect, it } from "vitest";
import { clientes, emprestimos, pagamentos, contratos, garantias, utilizadores, nomeCliente } from "./data";
describe("integridade dos dados demonstrativos", () => {
  it("gera volumes esperados", () => {
    expect(clientes).toHaveLength(50);
    expect(emprestimos).toHaveLength(120);
    expect(pagamentos).toHaveLength(300);
    expect(contratos).toHaveLength(80);
    expect(garantias).toHaveLength(60);
    expect(utilizadores).toHaveLength(10);
  });
  it("mantém referências válidas", () => {
    const idsClientes = new Set(clientes.map(c => c.id));
    const idsEmprestimos = new Set(emprestimos.map(e => e.id));
    expect(emprestimos.every(e => idsClientes.has(e.clienteId))).toBe(true);
    expect(pagamentos.every(p => idsEmprestimos.has(p.emprestimoId))).toBe(true);
    expect(contratos.every(c => idsEmprestimos.has(c.emprestimoId))).toBe(true);
    expect(garantias.every(g => idsEmprestimos.has(g.emprestimoId))).toBe(true);
  });
  it("resolve nomes de clientes e mantém valores positivos", () => {
    expect(nomeCliente(clientes[0].id)).toBe(clientes[0].nome);
    expect(emprestimos.every(e => e.valor > 0 && e.saldo >= 0)).toBe(true);
    expect(pagamentos.every(p => p.valor > 0)).toBe(true);
  });
});
