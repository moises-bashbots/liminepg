package cnab.cnab240;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * DARF Numerado / Arrecadação (FEBRABAN)
 *
 * Input: 48-digit linha digitável = 4 blocks of (11 data digits + 1 DV).
 * Output: 44-digit barcode reconstructed by removing the 4 block DVs.
 *
 * IMPORTANT:
 * For the DARF numerado sample (produto 858, segmento 7, órgão 0385),
 * the positions inside the 44-digit barcode that match your PDF are:
 *
 *  1-3   Produto (858)
 *  4     Segmento (7)
 *  5-15  Valor (11 dígitos, últimos 2 = centavos)
 *  16-19 Órgão (0385)
 *  20-44 Campo livre (25 dígitos)
 */
public class DarfNumerado {

    private final String linhaDigitavel48; // normalized 48 digits
    private final String codigoBarras44;   // reconstructed 44 digits

    // Parsed fields (layout for DARF numerado with órgão 0385)
    private final int produto;           // 1-3
    private final int segmento;          // 4
    private final long valorCentavos;    // 5-15 (11 digits)
    private final String orgao;          // 16-19 (4 digits)
    private final String campoLivre;     // 20-44 (25 digits)

    public DarfNumerado(String linhaDigitavel) {
        // 1) Normalize
        this.linhaDigitavel48 = normalize48(linhaDigitavel);

        // 2) Extract blocks: (11 data + 1 DV) x4
        String b1 = this.linhaDigitavel48.substring(0, 11);
        int dv1 = digit(this.linhaDigitavel48.charAt(11));

        String b2 = this.linhaDigitavel48.substring(12, 23);
        int dv2 = digit(this.linhaDigitavel48.charAt(23));

        String b3 = this.linhaDigitavel48.substring(24, 35);
        int dv3 = digit(this.linhaDigitavel48.charAt(35));

        String b4 = this.linhaDigitavel48.substring(36, 47);
        int dv4 = digit(this.linhaDigitavel48.charAt(47));

        // 3) Validate block DVs using MOD11 (matches your provided linha digitável)
        if (mod11Arrecadacao(b1) != dv1) throw new IllegalArgumentException("Block 1 DV mismatch.");
        if (mod11Arrecadacao(b2) != dv2) throw new IllegalArgumentException("Block 2 DV mismatch.");
        if (mod11Arrecadacao(b3) != dv3) throw new IllegalArgumentException("Block 3 DV mismatch.");
        if (mod11Arrecadacao(b4) != dv4) throw new IllegalArgumentException("Block 4 DV mismatch.");

        // 4) Build 44-digit barcode robustly by removing DV positions 11,23,35,47 (0-based)
        this.codigoBarras44 = buildBarcode44FromLinha48(this.linhaDigitavel48);

        // 5) Parse according to DARF numerado layout (as validated against your PDF)
        this.produto = Integer.parseInt(this.codigoBarras44.substring(0, 3)); // 1-3
        this.segmento = digit(this.codigoBarras44.charAt(3));                // 4

        // 5-15: value (11 digits), last 2 digits are cents
        String valor11 = this.codigoBarras44.substring(4, 15);
        this.valorCentavos = Long.parseLong(valor11);

        // 16-19: órgão
        this.orgao = this.codigoBarras44.substring(15, 19);

        // 20-44: campo livre (25 digits)
        this.campoLivre = this.codigoBarras44.substring(19, 44);
    }

    // ----------------------
    // Getters
    // ----------------------

    public String getLinhaDigitavel48() { return linhaDigitavel48; }
    public String getCodigoBarras44() { return codigoBarras44; }

    public int getProduto() { return produto; }
    public int getSegmento() { return segmento; }

    public long getValorCentavos() { return valorCentavos; }
    public double getValorReais() { return valorCentavos / 100.0; }

    public String getOrgao() { return orgao; }
    public String getCampoLivre() { return campoLivre; }

    // ----------------------
    // Human-friendly display
    // ----------------------

    public String show() {
        NumberFormat brl = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        StringBuilder sb = new StringBuilder();

        sb.append("\n==================== DARF / ARRECADAÇÃO ====================\n");
        sb.append("Linha digitável (48): ").append(linhaDigitavel48).append('\n');
        sb.append("Código de barras (44): ").append(codigoBarras44).append('\n');

        sb.append("\n--- Identificação ---\n");
        sb.append("Produto (1-3)........: ").append(produto);
        if (produto == 858) sb.append(" (Tributos / DARF)");
        sb.append('\n');

        sb.append("Segmento (4).........: ").append(segmento);
        if (segmento == 7) sb.append(" (Órgãos governamentais)");
        sb.append('\n');

        sb.append("\n--- Valores ---\n");
        sb.append("Valor (5-15).........: ").append(String.format("%011d", valorCentavos))
          .append(" (").append(brl.format(getValorReais())).append(")\n");

        sb.append("\n--- Órgão arrecadador ---\n");
        sb.append("Órgão (16-19).........: ").append(orgao);
        if ("0385".equals(orgao)) sb.append(" (Receita Federal do Brasil – DARF numerado)");
        sb.append('\n');

        sb.append("\n--- Campo livre ---\n");
        sb.append("Campo livre (20-44)..: ").append(campoLivre).append('\n');
        sb.append("Tamanho..............: ").append(campoLivre.length()).append(" dígitos\n");

        sb.append("\n--- Auditoria de posições (barcode 44) ---\n");
        sb.append("Barcode[1-3]  produto......: ").append(codigoBarras44.substring(0, 3)).append('\n');
        sb.append("Barcode[4]    segmento.....: ").append(codigoBarras44.charAt(3)).append('\n');
        sb.append("Barcode[5-15] valor(11)....: ").append(codigoBarras44.substring(4, 15)).append('\n');
        sb.append("Barcode[16-19] órgão(4)....: ").append(codigoBarras44.substring(15, 19)).append('\n');
        sb.append("Barcode[20-44] campo livre.: ").append(codigoBarras44.substring(19, 44)).append('\n');

        sb.append("\n============================================================\n");
        return sb.toString();
    }

    // ----------------------
    // Internal helpers
    // ----------------------

    private static String normalize48(String linhaDigitavel) {
        String s = (linhaDigitavel == null ? "" : linhaDigitavel).replaceAll("\\D", "");
        if (!s.matches("\\d{48}")) {
            throw new IllegalArgumentException("Linha digitável must contain exactly 48 numeric digits (11+DV)*4.");
        }
        return s;
    }

    /**
     * Builds the 44-digit barcode by removing each block DV.
     * DV positions in the 48-digit string (0-based): 11, 23, 35, 47.
     */
    private static String buildBarcode44FromLinha48(String linha48Normalized) {
        if (linha48Normalized == null || !linha48Normalized.matches("\\d{48}")) {
            throw new IllegalArgumentException("Expected normalized 48-digit numeric string.");
        }
        StringBuilder out = new StringBuilder(44);
        for (int i = 0; i < 48; i++) {
            if (i == 11 || i == 23 || i == 35 || i == 47) continue;
            out.append(linha48Normalized.charAt(i));
        }
        if (out.length() != 44) {
            throw new IllegalStateException("Internal error: expected 44 digits, got " + out.length());
        }
        return out.toString();
    }

    private static int digit(char c) {
        if (c < '0' || c > '9') throw new IllegalArgumentException("Invalid digit: " + c);
        return c - '0';
    }

    /**
     * Modulo 11 for arrecadação block DVs:
     * - weights 2..9 repeating, applied right-to-left
     * - dv = 11 - (sum % 11)
     * - if dv is 0, 10, or 11 => dv = 0
     */
    private static int mod11Arrecadacao(String block11) {
        int sum = 0;
        int w = 2;
        for (int i = block11.length() - 1; i >= 0; i--) {
            int n = block11.charAt(i) - '0';
            sum += n * w;
            w++;
            if (w > 9) w = 2;
        }
        int r = sum % 11;
        int dv = 11 - r;
        if (dv == 0 || dv == 10 || dv == 11) return 0;
        return dv;
    }

    // ----------------------
    // Verbose main: validates your DARF (R$ 757,12, órgão 0385)
    // ----------------------

    public static void main(String[] args) {
        // From your DARF PDF (linha digitável):
        // 85870000007 3 57120385253 3 49070125339 9 50958304284 3
        String linha48 = "858700000073571203852533490701253399509583042843";

        System.out.println("Input linha48: " + linha48);
        System.out.println("Length: " + linha48.replaceAll("\\D", "").length());

        DarfNumerado d = new DarfNumerado(linha48);

        // Full report
        System.out.println(d.show());

        // Assertions for this specific DARF
        long expectedCentavos = 75712L;  // R$ 757,12
        String expectedOrgao = "0385";   // RFB / DARF numerado

        if (d.getValorCentavos() != expectedCentavos) {
            throw new AssertionError("Expected valorCentavos=" + expectedCentavos + " but got " + d.getValorCentavos());
        }
        if (!expectedOrgao.equals(d.getOrgao())) {
            throw new AssertionError("Expected orgao=" + expectedOrgao + " but got " + d.getOrgao());
        }

        System.out.println("OK: value and órgão match expected for this DARF (R$ 757,12; órgão 0385).");
    }
}
