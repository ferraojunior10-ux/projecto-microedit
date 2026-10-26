# JCF Microcrédito — Protótipo React

Frontend demonstrativo para gestão de microcrédito em Moçambique. **Não utilizar dados reais ou processar empréstimos reais.**

## Executar
Requer Node.js 20.19+ ou 22.12+.
```bash
npm install
npm run dev
npm run build
```

## Módulos
Dashboard, Centro de Operações, Clientes, Solicitações, Empréstimos, Garantias, Contratos, Financeiro, Cobrança, Relatórios e Administração.

## Dados
50 clientes, 120 empréstimos, 300 pagamentos, 80 contratos, 60 garantias e 10 utilizadores simulados. Todos são gerados em `src/data.ts` e **não são persistidos**.

## Deploy
Importar este repositório na Vercel, framework Vite, diretório raiz `./`, comando `npm run build`, saída `dist`. `vercel.json` contém rewrite para rotas SPA.

## Limitações importantes
Não há autenticação, autorização, API, PostgreSQL, documentos, validação de identidade, trilho de auditoria ou proteção de dados pessoais. É apenas um protótipo visual. Antes de produção implementar backend, RBAC, segurança, testes automatizados e conformidade regulatória.
