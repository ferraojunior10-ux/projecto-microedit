/** API pública de diagnóstico. Nunca enviar credenciais administrativas pelo browser. */
export type ApiHealth = { status: string };
export const apiBaseUrl = (import.meta.env.VITE_API_BASE_URL as string | undefined)?.replace(/\/$/, "") ?? "";
export async function checkApiHealth(signal?: AbortSignal): Promise<"online" | "offline" | "unconfigured"> {
  if (!apiBaseUrl) return "unconfigured";
  try {
    const response = await fetch(apiBaseUrl + "/actuator/health", { signal, cache: "no-store" });
    if (!response.ok) return "offline";
    const data: unknown = await response.json();
    return typeof data === "object" && data !== null && "status" in data && data.status === "UP" ? "online" : "offline";
  } catch {
    return "offline";
  }
}
