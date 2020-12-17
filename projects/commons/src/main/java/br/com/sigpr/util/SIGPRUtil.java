package br.com.sigpr.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateFormatUtils;

public final class SIGPRUtil {

	private static final Locale LOCALE = new Locale("pt", "BR");
	private static final SimpleDateFormat SDF = new SimpleDateFormat();
	private static final NumberFormat CURRENCYFORMAT = NumberFormat.getCurrencyInstance(LOCALE);
	private static final NumberFormat NUMBERFORMAT = NumberFormat.getInstance(LOCALE);

	private SIGPRUtil() {
		// Construtor Vazio
	}

	public static String getAno(final Date data) {
		SDF.applyLocalizedPattern("yyyy");
		return SDF.format(data);
	}

	public static String getMes(final Date data) {
		SDF.applyLocalizedPattern("MM");
		return SDF.format(data);
	}

	public static String getDia(final Date data) {
		SDF.applyLocalizedPattern("dd");
		return SDF.format(data);
	}

	public static String formatarData(final Date data, final String pattern) {
		if (data == null) {
			return "";
		} else {
			SDF.applyLocalizedPattern(pattern);
			return SDF.format(data);
		}
	}

	public static String formatarMoeda(final BigDecimal valor) {

		BigDecimal retorno = valor;

		CURRENCYFORMAT.setMinimumFractionDigits(2);
		CURRENCYFORMAT.setMaximumFractionDigits(2);

		if (retorno == null) {
			retorno = BigDecimal.ZERO;
		}
		return CURRENCYFORMAT.format(retorno);
	}

	public static String formatarNumero(final BigDecimal numero) {
		if (numero == null) {
			return "";
		} else {
			NUMBERFORMAT.setMinimumFractionDigits(2);
			NUMBERFORMAT.setMaximumFractionDigits(2);

			return NUMBERFORMAT.format(numero);
		}
	}

	public static String formatarCPF(final Long nuCpf) {
		if (nuCpf == null) {
			return "";
		} else {
			String cpfFormatado = String.valueOf(nuCpf);

			while (cpfFormatado.length() < 11) {
				cpfFormatado = "0" + cpfFormatado;
			}
			return cpfFormatado.substring(0, 9) + "-" + cpfFormatado.substring(9, 11);
		}
	}

	public static String adicionaZerosEsquerda(final Integer numero, final int tamanho) {
		String valor = String.valueOf(numero);
		while (valor.length() < tamanho) {
			valor = "0" + valor;
		}
		return valor;
	}

	public static String formatarData(final String data, final String patternEntrada, final String patternSaida) {

		try {
			final Date date = DateFormat.getDateInstance(DateFormat.FULL).parse(data);

			return SIGPRUtil.formatarData(date, patternSaida);
		} catch (ParseException e) {
			return "";
		}

	}

	public static String formatarTelefone(final Long nuTelefone) {

		if (nuTelefone == null) {
			return "";
		} else {
			String telefoneFormatado = nuTelefone.toString();
			if (Long.valueOf(telefoneFormatado) > 0) {
				while (telefoneFormatado.length() != 10) {
					telefoneFormatado = "0" + telefoneFormatado;
				}
				return telefoneFormatado.substring(0, 2) + "-" + telefoneFormatado.substring(2, 6) + "-"
						+ telefoneFormatado.substring(6, 10);
			} else {
				return "";
			}
		}
	}
	
	public static Date getData(String data, String patternEntrada){
		
		if (data != null) {

			SDF.applyLocalizedPattern(patternEntrada);
			try {
				return SDF.parse(data);
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
		
	}
	
	public static Locale getLocale(){
		return LOCALE;
	}
	
	/** 
     * Calcula o tempo de execução em relação à data atual e a data de início. 
     * Retorna a concatenação das diferenças entre as horas e minutos. 
     *  
     * @return String hora:min 
     */  
    public static String calculaDiferenciaHoras(Date dataInicio, Date dataFim) {  

    	long timeInicio = dataInicio.getTime();
        long timeFim = dataFim.getTime();  
  
        return DateFormatUtils.formatUTC(timeFim - timeInicio, "HH:mm");  
    }
    
    /** 
     * Calcular diferenca em horas entre duas datas. 
     * 
     * @param dataInicio Data inicial.
     * @param dataFim Data final.
     * @return int O numero de horas. 
     */  
    public static double calculaDiferencaHoras(Date dataInicio, Date dataFim) {  
        return (dataFim.getTime() - dataInicio.getTime()) / (double) (1000*60*60);  
    }
    
}
