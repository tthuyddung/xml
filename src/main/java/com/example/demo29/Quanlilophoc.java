package com.example.demo29;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class Quanlilophoc {

    @FXML
    private TextField studIDTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TableView<Student> tableView;

    @FXML
    private TableColumn<Student, String> studIDColumn;

    @FXML
    private TableColumn<Student, String> nameColumn;

    @FXML
    private TableColumn<Student, String> addressColumn;

    @FXML
    private Button addButton;

    public void initialize() {
        // Khởi tạo cột trong bảng TableView
        studIDColumn.setCellValueFactory(cellData -> cellData.getValue().studIDProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
    }

    @FXML
    public void handleAddButtonAction() {
        // Lấy thông tin từ TextField
        String studID = studIDTextField.getText();
        String name = nameTextField.getText();
        String address = addressTextField.getText();

        // Thêm thông tin vào bảng TableView
        Student student = new Student(studID, name, address);
        tableView.getItems().add(student);

        // Lưu thông tin vào file XML
        StudentXMLWriter writer = new StudentXMLWriter();
        writer.saveToXML(student);
    }

    public class StudentXMLWriter {
        public void saveToXML(Student student) {
            try {
                // Kiểm tra nếu file XML đã tồn tại
                File file = new File("sinhvien.xml");
                boolean fileExists = file.exists();

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc;

                // Nếu file đã tồn tại, sử dụng nó để cập nhật dữ liệu
                if (fileExists) {
                    doc = docBuilder.parse(file);
                } else {
                    doc = docBuilder.newDocument();
                    Element rootElement = doc.createElement("students");
                    doc.appendChild(rootElement);
                }

                Element studentElement = doc.createElement("student");

                Element studIDElement = doc.createElement("studID");
                studIDElement.appendChild(doc.createTextNode(student.getStudID()));
                studentElement.appendChild(studIDElement);

                Element nameElement = doc.createElement("name");
                nameElement.appendChild(doc.createTextNode(student.getName()));
                studentElement.appendChild(nameElement);

                Element addressElement = doc.createElement("address");
                addressElement.appendChild(doc.createTextNode(student.getAddress()));
                studentElement.appendChild(addressElement);

                // Nếu file đã tồn tại, thêm thẻ sinh viên vào phần tử gốc
                if (fileExists) {
                    Node root = doc.getDocumentElement();
                    root.appendChild(studentElement);
                } else {
                    // Nếu file chưa tồn tại, tạo file mới và thêm phần tử sinh viên vào
                    Element rootElement = doc.getDocumentElement();
                    rootElement.appendChild(studentElement);
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(file);

                transformer.transform(source, result);
            } catch (ParserConfigurationException | TransformerException | IOException | SAXException e) {
                e.printStackTrace();
            }
        }
    }
}