package com.example.demo29;

import com.example.demo29.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
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

    private ObservableList<Student> studentList;

    public void initialize() {
        // Khởi tạo danh sách sinh viên
        studentList = FXCollections.observableArrayList();

        // Khởi tạo cột trong bảng TableView
        studIDColumn.setCellValueFactory(cellData -> cellData.getValue().studIDProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        // Khôi phục dữ liệu từ lần chạy trước đó (nếu có)
        restoreData();
    }

    @FXML
    public void handleAddButtonAction() {
        // Lấy thông tin từ TextField
        String studID = studIDTextField.getText();
        String name = nameTextField.getText();
        String address = addressTextField.getText();

        // Thêm sinh viên vào danh sách
        Student student = new Student(studID, name, address);
        studentList.add(student);

        // Lưu thông tin vào file XML
        saveToXML(student);

        // Xóa nội dung TextField sau khi thêm sinh viên thành công
        studIDTextField.clear();
        nameTextField.clear();
        addressTextField.clear();
    }

    private void saveToXML(Student student) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc;
            File file = new File("sinhvien.xml");
            boolean fileExists = file.exists();

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

            if (fileExists) {
                Node root = doc.getDocumentElement();
                root.appendChild(studentElement);
            } else {
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

    private void restoreData() {
        try {
            File file = new File("sinhvien.xml");
            if (file.exists()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(file);


                doc.getDocumentElement().normalize();
                Element rootElement = doc.getDocumentElement();
                NodeList nodeList = rootElement.getElementsByTagName("student");

                for (int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String studID = element.getElementsByTagName("studID").item(0).getTextContent();
                        String name = element.getElementsByTagName("name").item(0).getTextContent();
                        String address = element.getElementsByTagName("address").item(0).getTextContent();

                        Student student = new Student(studID, name, address);
                        studentList.add(student);
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        // Hiển thị danh sách sinh viên trong bảng TableView
        tableView.setItems(studentList);
    }



}