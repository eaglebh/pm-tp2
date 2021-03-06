package br.ufmg.dcc.pm.tp2;

import br.ufmg.dcc.pm.tp2.bibtex.*;
import br.ufmg.dcc.pm.tp2.persistence.BibFileIO;
import br.ufmg.dcc.pm.tp2.util.AuthorReader;
import br.ufmg.dcc.pm.tp2.util.PagesReader;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainWindow implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(MainWindow.class.getName());

    private ObservableList<BibtexFormat> model;
    private BibFileIO fileio;

    @FXML
    public Button pbRemove;
    @FXML
    public TextField leBookTitle;
    @FXML
    public Label lbBookTitle;
    @FXML
    public MenuItem action_Novo;
    @FXML
    public MenuItem action_Sair;
    @FXML
    public TextField leQuery;
    @FXML
    public ComboBox cbSearch;
    @FXML
    public Button pbSearch;
    @FXML
    public ListView<BibtexFormat> lwAllBibtex;
    @FXML
    public Label lbReference;
    @FXML
    public TextField leReference;
    @FXML
    public Label lbAuthor;
    @FXML
    public TextField leAuthor;
    @FXML
    public Label lbTitle;
    @FXML
    public TextField leTitle;
    @FXML
    public Label lbYear;
    @FXML
    public TextField leYear;
    @FXML
    public Label lbPublisher;
    @FXML
    public TextField lePublisher;
    @FXML
    public Label lbJournal;
    @FXML
    public TextField leJournal;
    @FXML
    public Label lbVolume;
    @FXML
    public TextField leVolume;
    @FXML
    public Label lbNumber;
    @FXML
    public TextField leNumber;
    @FXML
    public Label lbPages;
    @FXML
    public TextField lePages;
    @FXML
    public Button pbSave;
    private ListChangeListener<BibtexFormat> allBibTexListener;
    private Stage stage;
    private String file;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void handlePbSaveAction(ActionEvent actionEvent) {
        saveButtonClicked();
    }

    public void handlePbRemoveAction(ActionEvent actionEvent) {
        removeButtonClicked();
    }

    public void handlePbSearchAction(ActionEvent actionEvent) {
        searchButtonClicked();
    }

    public MainWindow() {

    }

    public void filterAllBibtex() {
        List<BibtexFormat> bibs = fileio.getBibFile().getBibs();
        model = FXCollections.observableArrayList(bibs);
        lwAllBibtex.setItems(model);
        lwAllBibtex.getSelectionModel().getSelectedItems().addListener(allBibTexListener);
        lwAllBibtex.getSelectionModel().selectFirst();
    }

    public void filterOneBibtex() {
        try {
            BibFile.SearchCriteria criteria = BibFile.SearchCriteria.fromInteger(cbSearch.getSelectionModel().getSelectedIndex());
            BibtexFormat bib = fileio.getBibFile().retrieveBibtex(criteria, leQuery.getText());
            List<BibtexFormat> list = new ArrayList<BibtexFormat>();
            list.add(bib);
            model = FXCollections.observableArrayList(list);
            lwAllBibtex.setItems(model);
            lwAllBibtex.getSelectionModel().getSelectedItems().addListener(allBibTexListener);
            lwAllBibtex.getSelectionModel().selectFirst();
        } catch (Exception e) {
            LOGGER.fine("ERRO: " + e.getLocalizedMessage());
            Dialogs.showInformationDialog(stage, "Não encontrou BibTex.", null, "PMCC-TP2");
        }
    }

    private String checkRequiredField(TextField field) throws RequiredFieldException {
        String text = field.getText();
        if (text.isEmpty()) {
            throw new RequiredFieldException(field);
        }
        return text;
    }

    void saveBibtex(BibtexFormat bib) throws RequiredFieldException {
        if (bib instanceof BibtexArticle) {
            ((BibtexArticle) bib).setJournal(checkRequiredField(leJournal));
            ((BibtexArticle) bib).setVolume(Integer.parseInt(checkRequiredField(leVolume)));
            ((BibtexArticle) bib).setNumber(Integer.parseInt(checkRequiredField(leNumber)));
            try {
                ((BibtexArticle) bib).setPages(PagesReader.parsePagesFromValue(checkRequiredField(lePages)));
            } catch (Exception e) {
                e.printStackTrace();
                Dialogs.showInformationDialog(stage, "Erro ao salvar BibTex.", null, "PMCC-TP2");
                return;
            }
        } else {
            if (bib instanceof BibtexBook) {
                ((BibtexBook) bib).setPublisher(checkRequiredField(lePublisher));
            } else {
                if (bib instanceof BibtexInproceedings) {
                    ((BibtexInproceedings) bib).setBooktitle(checkRequiredField(leBookTitle));
                    try {
                        ((BibtexInproceedings) bib).setPages(PagesReader.parsePagesFromValue(checkRequiredField(lePages)));
                    } catch (Exception e) {
                        e.printStackTrace();
                        Dialogs.showInformationDialog(stage, "Erro ao salvar BibTex.", null, "PMCC-TP2");
                        return;
                    }
                }
            }
        }
        bib.setAuthors(AuthorReader.parseAuthorsFromValue(checkRequiredField(leAuthor)));
        bib.setTitle(checkRequiredField(leTitle));
        bib.setYear(Long.parseLong(checkRequiredField(leYear)));
        bib.setReference(checkRequiredField(leReference));
        try {
            fileio.persist();
        } catch (IOException e) {
            e.printStackTrace();
            Dialogs.showInformationDialog(stage, "Erro ao persistir modificações.", null, "PMCC-TP2");
        }
        LOGGER.fine("salvou ");
        filterAllBibtex();
    }

    public void selectionChangedSlot() {
        //get the text of the selected item
        BibtexFormat bib = lwAllBibtex.getSelectionModel().getSelectedItem();

        if (bib == null) {
            return;
        }
        hideAllInputs();
        clearAllInputs();

        try {
            if (bib instanceof BibtexArticle) {
                showArticleInputs();
                leJournal.setText(((BibtexArticle) bib).getJournal());
                leVolume.setText(String.valueOf(((BibtexArticle) bib).getVolume()));
                leNumber.setText(String.valueOf(((BibtexArticle) bib).getNumber()));
                lePages.setText(Pages.getPagesText(((BibtexArticle) bib).getPages()));
            } else {
                if (bib instanceof BibtexBook) {
                    showBookInputs();
                    lePublisher.setText(((BibtexBook) bib).getPublisher());
                } else {
                    if (bib instanceof BibtexInproceedings) {
                        showInproceedingsInputs();
                        leBookTitle.setText(((BibtexInproceedings) bib).getBooktitle());
                        lePages.setText(Pages.getPagesText(((BibtexInproceedings) bib).getPages()));
                    }
                }
            }
            leAuthor.setText(bib.getAuthors().getAuthorsText());
            leTitle.setText(bib.getTitle());
            leYear.setText(String.valueOf(bib.getYear()));
            leReference.setText(bib.getReference());
        } catch (Exception e) {
            LOGGER.fine("ERRO: " + e.getLocalizedMessage());
            Dialogs.showInformationDialog(stage, "Erro na seleção de tipo.", null, "PMCC-TP2");
        }
    }

    public void saveButtonClicked() {
        if (leReference.getText().isEmpty()) {
            Dialogs.showErrorDialog(stage, "É preciso preencher a referência.", null, "PMCC-TP2");
            return;
        }
        BibtexFormat bib = null;
        try {
            bib = fileio.getBibFile().retrieveBibtex(BibFile.SearchCriteria.REFERENCE, leReference.getText());
            saveBibtex(bib);
        } catch (Exception e) {
            Dialogs.DialogResponse response = Dialogs.showConfirmDialog(stage,
                    "Não encontrou BibTex modificado para salvar. Deseja salvar como novo?",
                    null, "PMCC-TP2", Dialogs.DialogOptions.YES_NO);

            if (response == Dialogs.DialogResponse.YES) {
                try {
                    bib = fileio.getBibFile().retrieveBibtex(BibFile.SearchCriteria.REFERENCE, "");
                } catch (Exception e1) {
                    LOGGER.warning(e1.getLocalizedMessage());
                    Dialogs.showInformationDialog(stage, "Não encontrou BibTex modificado para salvar e nenhum foi criado.", null, "PMCC-TP2");
                }
                try {
                    saveBibtex(bib);
                } catch (RequiredFieldException e1) {
                    e1.showMessage();
                }
            } else {
                Dialogs.showInformationDialog(stage, "Não encontrou BibTex modificado para salvar e nenhum foi criado.", null, "PMCC-TP2");
            }
        } catch (RequiredFieldException e) {
            e.showMessage();
        }
    }

    public void removeButtonClicked() {
        try {
            BibtexFormat bib = lwAllBibtex.getSelectionModel().getSelectedItem();
            fileio.getBibFile().deleteBibtex(bib);
            fileio.persist();
            filterAllBibtex();
        } catch (Exception e) {
            LOGGER.fine("ERRO: " + e.getLocalizedMessage());
            Dialogs.showInformationDialog(stage, "Não encontrou BibTex modificado para remover!", null, "PMCC-TP2");
        }
    }

    public void newButtonClicked() {
        List<String> bibtexTypes = new ArrayList<String>();
        String book = "Livro";
        String article = "Artigo";
        String inproceeding = "Artigo de Conf.";
        bibtexTypes.add(book);
        bibtexTypes.add(article);
        bibtexTypes.add(inproceeding);
        String type = Dialogs.showInputDialog(stage, "Registro Bibtex:",
                null, "Selecione o tipo", "Livro", bibtexTypes);
        LOGGER.fine(type);
        BibtexFormat bib;

        filterAllBibtex();
        clearAllInputs();
        hideAllInputs();
        if (type.equals(book)) {
            bib = new BibtexBook();
            showBookInputs();
        } else {
            if (type.equals(article)) {
                bib = new BibtexArticle();
                showArticleInputs();
            } else {
                if (type.equals(inproceeding)) {
                    bib = new BibtexInproceedings();
                    showInproceedingsInputs();
                } else {
                    Dialogs.showInformationDialog(stage, "Tipo desconhecido de BibTex.", null, "PMCC-TP2");
                    return;
                }
            }
        }
        fileio.getBibFile().createBibtex(bib);
    }

    public void searchButtonClicked() {
        model.clear();
        switch (BibFile.SearchCriteria.fromInteger(cbSearch.getSelectionModel().getSelectedIndex())) {
            case ALL:
                filterAllBibtex();
                break;
            default:
                filterOneBibtex();
                break;
        }
    }

    public void hideAllInputs() {
        leJournal.setVisible(false);
        leVolume.setVisible(false);
        leNumber.setVisible(false);
        lePages.setVisible(false);
        lePublisher.setVisible(false);
        leBookTitle.setVisible(false);
        lePages.setVisible(false);
        lbJournal.setVisible(false);
        lbVolume.setVisible(false);
        lbNumber.setVisible(false);
        lbPages.setVisible(false);
        lbPublisher.setVisible(false);
        lbBookTitle.setVisible(false);
        lbPages.setVisible(false);
    }

    public void showArticleInputs() {
        leJournal.setVisible(true);
        leVolume.setVisible(true);
        leNumber.setVisible(true);
        lePages.setVisible(true);
        lbJournal.setVisible(true);
        lbVolume.setVisible(true);
        lbNumber.setVisible(true);
        lbPages.setVisible(true);
        showBasicInputs();
    }

    public void showBookInputs() {
        lePublisher.setVisible(true);
        lbPublisher.setVisible(true);
        showBasicInputs();
    }

    public void showInproceedingsInputs() {
        leBookTitle.setVisible(true);
        lePages.setVisible(true);
        lbBookTitle.setVisible(true);
        lbPages.setVisible(true);
        showBasicInputs();
    }

    public void showBasicInputs() {
        leAuthor.setVisible(true);
        leTitle.setVisible(true);
        leYear.setVisible(true);
        leReference.setVisible(true);
        lbAuthor.setVisible(true);
        lbTitle.setVisible(true);
        lbYear.setVisible(true);
        lbReference.setVisible(true);
    }

    public void clearAllInputs() {
        leAuthor.clear();
        leTitle.clear();
        leYear.clear();
        leReference.clear();
        leJournal.clear();
        leVolume.clear();
        leNumber.clear();
        lePages.clear();
        lePublisher.clear();
        leBookTitle.clear();
        lePages.clear();
    }

    public void onNovoClicked(ActionEvent actionEvent) {
        newButtonClicked();
    }

    public void onSairClicked(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setFile(String file) {
        this.file = file;
        try {
            fileio = new BibFileIO(file);
            allBibTexListener = new ListChangeListener<BibtexFormat>() {
                @Override
                public void onChanged(Change<? extends BibtexFormat> change) {
                    selectionChangedSlot();
                }
            };
            filterAllBibtex();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Dialogs.showInformationDialog(stage, "Erro ao ler arquivo BibTex.", null, "PMCC-TP2");
        }
    }

    private class RequiredFieldException extends Throwable {
        private final String toolTip;

        public RequiredFieldException(TextField field) {
            toolTip = field.getPromptText();
        }

        public void showMessage() {
            Dialogs.showErrorDialog(stage, "Favor preencher o campo " + toolTip, null, "PMCC-TP2");
        }
    }
}
