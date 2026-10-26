import { afterEach, describe, expect, it, vi } from "vitest";
import { checkApiHealth } from "./api";
afterEach(() => vi.unstubAllGlobals());
describe("API health", () => {
  it("não faz pedidos quando a API não está configurada", async () => {
    const fetchMock = vi.fn();
    vi.stubGlobal("fetch", fetchMock);
    expect(await checkApiHealth()).toBe("unconfigured");
    expect(fetchMock).not.toHaveBeenCalled();
  });
});
