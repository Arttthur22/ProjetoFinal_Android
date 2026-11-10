# ProjetoFinal_Android

# CineCatalog
App Android que mostra o catálogo de filmes do Studio Ghibli, permite buscar por título ou diretor, ver detalhes e guardar favoritos por conta de usuário, com login local.

## API utilizada
**Studio Ghibli API** — `https://ghibliapi.vercel.app/` (sem cadastro e sem chave).
Usamos `GET /films` (lista com título, diretor, ano, duração, nota Rotten Tomatoes, sinopse, pôster e banner) e `GET /films/{id}` (detalhe).

## Prints

| Login | Cadastro | Lista |
|---|---|---|
| <img width="331" height="784" alt="image" src="https://github.com/user-attachments/assets/20ad3803-d283-4789-9d3c-d7a83d9357b5" /> | <img width="346" height="781" alt="image" src="https://github.com/user-attachments/assets/5d26278d-e8c5-4a1d-9288-034e2587278c" />  | <img width="337" height="777" alt="image" src="https://github.com/user-attachments/assets/45280bba-7743-4387-8810-a26a4e571234" />


| Detalhe | Favoritos | Estado de erro |
|---|---|---|
| <img width="399" height="912" alt="image" src="https://github.com/user-attachments/assets/1514793a-e60b-47b0-a7e1-c3ee343f3fd4" /> | <img width="404" height="903" alt="image" src="https://github.com/user-attachments/assets/b0935b13-f94e-4105-bf66-fbb4c080f1d5" /> | <img width="398" height="944" alt="image" src="https://github.com/user-attachments/assets/29522b75-ba1a-47a3-a340-a5e5c097de67" />



## Divisão de tarefas
| Integrante | O que fez |
|------------|-----------|
| _Arthur Raymundo_ | _network e data: Retrofit, Room, Repositories e usuários_ |
| _Giovana Simões_ | _viewmodel e userinterface: telas e navegação_ |
| _Gustavo Lopes_ | _Wireframe_ |

## O que aprendemos
- _Arthur Raymundo:_ uma frase sobre a sua parte.
- _Giovana Simões:_ uma frase sobre a sua parte.
- _Gustavo Lopes:_ uma frase sobre a sua parte.

---

## Como o app atende aos requisitos

| Requisito | Onde está |
|---|---|
| API REST (Retrofit + coroutines) | `network/GhibliApi.kt`, `network/RetrofitInstance.kt`, `data/MovieRepository.kt` (chamadas `suspend` em `Dispatchers.IO`). As telas mostram **carregando / sucesso / erro** (`MoviesUiState`, `DetailUiState`) com botão "Tentar novamente". |
| Login e sessão | `data/AuthRepository.kt` (cadastro, login, validação), `data/SessionManager.kt` (id do usuário em SharedPreferences), botão **Sair** em Perfil. Senha salva só como **hash SHA-256** (`data/Security.kt`). |
| Persistência local (Room) | `data/AppDatabase.kt`, `Usuario`/`UsuarioDao`, `Favorito`/`FavoritoDao`. Favoritos pertencem ao `usuarioId` logado e guardam uma cópia do filme, então abrem **offline**. |
| Navegação | `userinterface/AppNavigation.kt`: barra inferior com **4 abas** (Início, Buscar, Favoritos, Perfil), fluxo Login → app, e `DetailRoute(movieId)` **type-safe** para a tela de detalhe. |

## Estrutura de pacotes

```
├─ network        GhibliApi, FilmDto, RetrofitInstance
├─ data           Movie, Usuario, Favorito, DAOs, AppDatabase, AuthRepository,
│                 MovieRepository, SessionManager, Validation, Security, AppContainer
├─ viewmodel      AuthViewModel, MoviesViewModel, DetailViewModel, AppViewModelProvider
└─ userinterface  MainActivity, AppNavigation, telas Compose e theme/
```

Regra de Dependência conferida: nenhuma busca por `import retrofit2` em `userinterface` e nenhuma por `userinterface` em `viewmodel` retorna resultado.

## Regras do login
- E-mail com formato válido; senha com 6 ou mais caracteres; confirmação de senha igual.
- E-mail único (comparado em minúsculas).
- Falha de login mostra sempre "E-mail ou senha incorretos." (não revela qual campo errou).

## Como rodar
1. Abra a pasta no Android Studio e aguarde o Sync do Gradle.
2. Rode em um emulador ou aparelho (minSdk 24).
3. Para testar o estado de erro, ligue o **modo avião** e toque em "Tentar novamente".

Testes unitários: `./gradlew test` (validação e hash).

## Roteiro de teste (igual ao do professor)
criar conta → entrar com senha errada → entrar corretamente → percorrer as abas → abrir um detalhe → favoritar → modo avião (estado de erro) → fechar e reabrir (continua logado, favorito salvo) → Sair → entrar com outra conta (favoritos não se misturam).
