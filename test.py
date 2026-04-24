from transformers import pipeline

chatbot = pipeline("text-generation", model="gpt2")

prompt = "El presidente de Marte en 2025 es"
respuesta = chatbot(prompt, max_length=30)

print(respuesta)