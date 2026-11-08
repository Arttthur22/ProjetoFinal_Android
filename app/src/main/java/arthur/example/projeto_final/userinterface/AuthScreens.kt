package arthur.example.projeto_final.userinterface

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arthur.example.projeto_final.userinterface.theme.AccentSoft
import arthur.example.projeto_final.userinterface.theme.Ink
import arthur.example.projeto_final.userinterface.theme.Muted
import arthur.example.projeto_final.userinterface.theme.Positive
import arthur.example.projeto_final.viewmodel.AuthUiState

@Composable
fun LoginScreen(
    state: AuthUiState,
    registeredNotice: Boolean,
    onLogin: (email: String, senha: String) -> Unit,
    onGoToRegister: () -> Unit,
    onEnter: () -> Unit
) {
    LaunchedEffect(Unit) { onEnter() }
    var email by rememberSaveableText()
    var senha by rememberSaveableText()

    AuthScaffold(
        eyebrow = "BEM-VINDO DE VOLTA",
        title = "Entre na sua conta",
        subtitle = "Descubra histórias que ficam com você."
    ) {
        if (registeredNotice && state.error == null) {
            Text("Conta criada com sucesso! Entre para continuar.", color = Positive, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
        AppTextField(email, { email = it }, "E-mail", "voce@email.com", keyboardType = KeyboardType.Email, isError = state.error != null)
        AppTextField(
            senha, { senha = it }, "Senha", "••••••••",
            isPassword = true, imeAction = ImeAction.Done, isError = state.error != null,
            onDone = { onLogin(email, senha) }
        )
        state.error?.let { Text(it, color = AccentSoft, fontSize = 12.sp) }
        PrimaryButton(
            text = if (state.loading) "Aguarde..." else "Entrar",
            onClick = { onLogin(email, senha) },
            loading = state.loading,
            modifier = Modifier.fillMaxWidth()
        )
        SwapLine("Ainda não tem conta?", "Criar conta", onGoToRegister)
    }
}

@Composable
fun RegisterScreen(
    state: AuthUiState,
    onRegister: (nome: String, email: String, senha: String, confirmacao: String) -> Unit,
    onGoToLogin: () -> Unit,
    onEnter: () -> Unit
) {
    LaunchedEffect(Unit) { onEnter() }
    var nome by rememberSaveableText()
    var email by rememberSaveableText()
    var senha by rememberSaveableText()
    var confirmacao by rememberSaveableText()

    AuthScaffold(
        eyebrow = "NOVO POR AQUI?",
        title = "Crie sua conta",
        subtitle = "Sua próxima grande história começa aqui."
    ) {
        AppTextField(nome, { nome = it }, "Nome completo", "Como podemos te chamar?")
        AppTextField(email, { email = it }, "E-mail", "voce@email.com", keyboardType = KeyboardType.Email)
        AppTextField(senha, { senha = it }, "Senha", "Mínimo de 6 caracteres", isPassword = true)
        AppTextField(
            confirmacao, { confirmacao = it }, "Confirmar senha", "Repita sua senha",
            isPassword = true, imeAction = ImeAction.Done,
            onDone = { onRegister(nome, email, senha, confirmacao) }
        )
        state.error?.let { Text(it, color = AccentSoft, fontSize = 12.sp) }
        PrimaryButton(
            text = if (state.loading) "Aguarde..." else "Criar conta",
            onClick = { onRegister(nome, email, senha, confirmacao) },
            loading = state.loading,
            modifier = Modifier.fillMaxWidth()
        )
        SwapLine("Já tem uma conta?", "Voltar ao login", onGoToLogin)
    }
}

@Composable
private fun rememberSaveableText() =
    androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf("") }

@Composable
private fun AuthScaffold(
    eyebrow: String,
    title: String,
    subtitle: String,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Box(Modifier.fillMaxSize().background(Ink)) {
        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 34.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(14.dp)
        ) {
            BrandLogo()
            Spacer(Modifier.height(20.dp))
            Text(eyebrow, style = MaterialTheme.typography.labelSmall, color = AccentSoft)
            Text(title, style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Text(subtitle, fontSize = 13.sp, color = Muted)
            Spacer(Modifier.height(6.dp))
            content()
            Text(
                "Ao continuar, você concorda com os Termos de uso e a Política de privacidade.",
                fontSize = 9.sp, lineHeight = 14.sp, color = Color(0xFF626C7F),
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
            )
        }
    }
}

@Composable
private fun SwapLine(question: String, action: String, onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text("$question ", fontSize = 12.sp, color = Muted)
        Text(action, fontSize = 12.sp, color = AccentSoft, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onClick))
    }
}
