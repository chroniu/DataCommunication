import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application implements MessageListener {
	private static String CHAR_SET = "ISO-8859-1";
	
	private TextArea textArreaManchesterRecebido;
	private TextArea textArreaAsciiRecebido;
	private TextArea textArreaMensagemRecebida;
	
	@Override
	public void start(Stage stage) throws Exception {

		stage.setTitle("Comunicação de Dados - Manchester Diferencial");

		final Label labelIp = new Label("Endereço");
		final Label labelPorta = new Label("Porta");

		final TextField textFieldIpAddress = new TextField("224.0.0.1");
		final TextField textFielPorta = new TextField("6000");
		
		Button btnSetMulticast = new Button("Configurar");
		btnSetMulticast.setOnAction(action -> {
			try {
				MultiCastServer.replaceInstance(textFieldIpAddress.getText(), Integer.parseInt(textFielPorta.getText()),
						this);
				(new Thread(MultiCastServer.getInstance())).start();
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Erro");
				alert.setHeaderText("Erro ao criar Servlet");
				alert.setContentText(e.getMessage());

				alert.showAndWait();

				e.printStackTrace();
			}
		});
		
		textFieldIpAddress.setPrefColumnCount(16);
		textFielPorta.setPrefColumnCount(10);

		FlowPane flowPaneConfigMulticast = new FlowPane();

		flowPaneConfigMulticast.getChildren().add(labelIp);
		flowPaneConfigMulticast.getChildren().add(textFieldIpAddress);
		flowPaneConfigMulticast.getChildren().add(labelPorta);
		flowPaneConfigMulticast.getChildren().add(textFielPorta);
		flowPaneConfigMulticast.getChildren().add(btnSetMulticast);

		flowPaneConfigMulticast.setHgap(10);
		flowPaneConfigMulticast.setVgap(10);
//		flowPaneConfigMulticast.setBackground();
		flowPaneConfigMulticast.setStyle("-fx-background-color: #00FF00");
		////////////////////////////////////////////////////////// PAnel ASCII Envio
		final Label labelCoded = new Label("String binária");
		final TextArea textArreaAscii = new TextArea();
		textArreaAscii.setPrefColumnCount(43);
		textArreaAscii.setPrefRowCount(4);
		
		FlowPane flowPaneByteCoded = new FlowPane();
		flowPaneByteCoded.getChildren().add(labelCoded);
		flowPaneByteCoded.getChildren().add(textArreaAscii);
		flowPaneByteCoded.setHgap(10);
		flowPaneByteCoded.setVgap(10);
		////////////////////////////////////////////////////////// Panel String Envio
		////////////////////////////////////////////////////////// Manchester
		////////////////////////////////////////////////////////// Diferencial
		final Label labelManchester = new Label("Manchester: ");
		final TextArea textArreaManchester = new TextArea();
		textArreaManchester.setPrefColumnCount(43);
		textArreaManchester.setPrefRowCount(4);

		
		FlowPane flowPaneManchester = new FlowPane();
		flowPaneManchester.getChildren().add(labelManchester);
		flowPaneManchester.getChildren().add(textArreaManchester);
		flowPaneManchester.setHgap(10);
		flowPaneManchester.setVgap(10);

		//////////////////////////////////////////////////////////////// Envio
		//////////////////////////////////////////////////////////////// de
		//////////////////////////////////////////////////////////////// mensagem
		final Label labelMessage = new Label("Mensagem");
		final TextField textFielMessage = new TextField("Mensagem a ser enviada");
		textFielMessage.setPrefColumnCount(35);

		Button btnSendData = new Button("enviar");
		
		btnSendData.setOnAction(action -> {
			try {
				byte [] ascii  = DifferentialManchester.getByteRepresentation(textFielMessage.getText(), CHAR_SET);
				boolean[] asciiBool = DifferentialManchester.byteArrayToBinaryArray(ascii);
				boolean[] dataSend = DifferentialManchester.encodeToDifferentialManchester(asciiBool);
				byte [] data = DifferentialManchester.booleanArrayToByteArray(dataSend);
				
				textArreaAscii.setText(DifferentialManchester.booleanArrayToString(asciiBool));
				textArreaManchester.setText(DifferentialManchester.booleanArrayToString(dataSend));
				MultiCastServer.getInstance().sendMessage(data);
			} catch (Exception e) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Erro");
				alert.setHeaderText("Erro ao enviar Servlet");
				alert.setContentText(e.getMessage());
				alert.showAndWait();
				e.printStackTrace();
			}
		});
		
		FlowPane flowPaneSendData= new FlowPane();
		flowPaneSendData.getChildren().add(labelMessage);
		flowPaneSendData.getChildren().add(textFielMessage);
		flowPaneSendData.getChildren().add(btnSendData);
		
		flowPaneSendData.setHgap(10);
		flowPaneSendData.setVgap(10);
		
		//////////////////////////////////////////////////////////Panel String Recebimento
		////////////////////////////////////////////////////////// Manchester
		////////////////////////////////////////////////////////// Diferencial
		final Label labelManchesterRecebido = new Label("Manchester Recebido");
		textArreaManchesterRecebido = new TextArea();
		textArreaManchesterRecebido.setPrefColumnCount(43);
		textArreaManchesterRecebido.setPrefRowCount(4);
		
		final Label labelAsciiRecebido = new Label("String binária Recebida");
		textArreaAsciiRecebido = new TextArea();
		textArreaAsciiRecebido.setPrefColumnCount(43);
		textArreaAsciiRecebido.setPrefRowCount(4);

		
		final Label labelMensagemRecebida = new Label("Mensagem Recebida");
		textArreaMensagemRecebida = new TextArea();
		textArreaMensagemRecebida.setPrefColumnCount(43);
		textArreaMensagemRecebida.setPrefRowCount(4);
		textArreaMensagemRecebida.setPrefRowCount(4);

		
		FlowPane flowPaneManchesterRecebido = new FlowPane();
		flowPaneManchesterRecebido.getChildren().add(labelManchesterRecebido);
		flowPaneManchesterRecebido.getChildren().add(textArreaManchesterRecebido);
		flowPaneManchesterRecebido.getChildren().add(labelAsciiRecebido);
		flowPaneManchesterRecebido.getChildren().add(textArreaAsciiRecebido);
		flowPaneManchesterRecebido.getChildren().add(labelMensagemRecebida);
		flowPaneManchesterRecebido.getChildren().add(textArreaMensagemRecebida);
		
		flowPaneManchesterRecebido.setHgap(10);
		flowPaneManchesterRecebido.setVgap(10);

		flowPaneManchesterRecebido.setStyle("-fx-background-color: #FFFF00");
		///////////////////////////////////////////////////////////////////////////// Monta
		///////////////////////////////////////////////////////////////////////////// a
		///////////////////////////////////////////////////////////////////////////// cena

		VBox vbox = new VBox(flowPaneConfigMulticast, flowPaneSendData, flowPaneByteCoded, flowPaneManchester, flowPaneManchesterRecebido);
		Scene scene = new Scene(vbox, 600, 700);
		stage.setScene(scene);
		stage.show();
		stage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void messageReceivedUpdate(byte[] data) {
		boolean [] manchesterEnconded = DifferentialManchester.byteArrayToBinaryArray(data);
		boolean [] manchesterDecoded = DifferentialManchester.decodeFromDifferentialManchester(manchesterEnconded);
		byte [] codedData = DifferentialManchester.booleanArrayToByteArray(manchesterDecoded);
		textArreaManchesterRecebido.setText(DifferentialManchester.booleanArrayToString(manchesterEnconded));
		textArreaAsciiRecebido.setText(DifferentialManchester.booleanArrayToString(manchesterDecoded));
		textArreaMensagemRecebida.setText(DifferentialManchester.getStringFromByteArray(codedData, CHAR_SET));

	}

}
