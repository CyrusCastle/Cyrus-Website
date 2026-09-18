package uk.cyruscastle.www.view.screen.static

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cyruswebsite.shared.generated.resources.Res
import cyruswebsite.shared.generated.resources.phoneEmail
import org.jetbrains.compose.resources.imageResource
import uk.cyruscastle.www.controller.Navigator
import uk.cyruscastle.www.model.Control
import uk.cyruscastle.www.model.MultiTapInput
import uk.cyruscastle.www.view.phone.components.onscreen.MultiTapTextField
import uk.cyruscastle.www.view.screen.App
import uk.cyruscastle.www.view.screen.ScreenScaffold
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

enum class EmailInput {
    TO, SUBJECT, BODY;

    fun shifted(step: Int = 1): EmailInput = entries[(ordinal + step).mod(entries.size)]
}

class EmailApp  : App(
    name = "Email",
    icon = Res.drawable.phoneEmail,
    content = {
        val scope = rememberCoroutineScope()
        val recipientInput = remember { MultiTapInput(scope, startingText = "cyrusrobc@gmail.com") }
        val subjectInput = remember { MultiTapInput(scope, startingText = "On your website...") }
        val bodyInput = remember { MultiTapInput(scope) }

        var inputType by remember { mutableStateOf(EmailInput.BODY) }

        ScreenScaffold(
            leftButtonLabel = "Send",
            rightButtonLabel = "Exit",
            onControl = { control ->
                when (control) {
                    Control.PRIMARY_RIGHT -> Navigator.pop()
                    Control.PRIMARY_LEFT -> { sendEmail(recipientInput.value.text, subjectInput.value.text, bodyInput.value.text); Navigator.pop() }

                    Control.Up -> { inputType = inputType.shifted(-1); true }
                    Control.Down -> { inputType = inputType.shifted(1); true }

                    else -> when (inputType) {
                        EmailInput.TO -> { recipientInput.onControl(control) }
                        EmailInput.SUBJECT -> { subjectInput.onControl(control) }
                        EmailInput.BODY -> { bodyInput.onControl(control) }
                    }
                }
            }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.height(50.dp).fillMaxWidth()
                ) {
                    Image(
                        bitmap = imageResource(Res.drawable.phoneEmail),
                        contentDescription = null
                    )

                    Text(
                        text = "Email",
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }

                InputRow("To", recipientInput, inputType == EmailInput.TO) { inputType = EmailInput.TO }
                Spacer(Modifier.height(5.dp))

                InputRow("Subject", subjectInput, inputType == EmailInput.SUBJECT) { inputType = EmailInput.SUBJECT }
                Spacer(Modifier.height(5.dp))

                MultiTapTextField(
                    input = bodyInput,
                    textColor = Color.White,
                    caretColor = if (inputType == EmailInput.BODY) Color.White else Color.Transparent,
                    ruleColor = Color.Black.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth(0.8f).weight(1f).clickable { inputType = EmailInput.BODY }
                )
            }
        }
    }
)

@Composable
fun InputRow(title: String, input: MultiTapInput, isActive: Boolean, setActive: () -> Unit){
    Row(Modifier.fillMaxWidth(0.6f).clickable(onClick = setActive)) {
        Text(
            text = title,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(Modifier.width(5.dp))

        MultiTapTextField(
            input = input,
            textColor = Color.White,
            caretColor = if (isActive) Color.White else Color.Transparent,
            modifier = Modifier.weight(1f).border(1.dp, Color.Black).padding(horizontal = 5.dp)
        )
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
fun sendEmail(addressee: String, subject: String, content: String){
    js(
        $$"""
    const url = `mailto:${encodeURIComponent(addressee)}?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(content)}`;
    console.log(subject);
    console.log(url);
    window.open(url, '_blank');
    """)
}