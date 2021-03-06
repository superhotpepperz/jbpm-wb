/*
 * Copyright 2017 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.workbench.pr.client.editors.instance.list;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwtmockito.GwtMock;
import com.google.gwtmockito.GwtMockitoTestRunner;
import org.assertj.core.data.Index;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.jboss.errai.ioc.client.api.ManagedInstance;
import org.jbpm.workbench.common.client.list.*;
import org.jbpm.workbench.common.client.util.DateUtils;
import org.jbpm.workbench.common.client.util.GenericErrorSummaryCountCell;
import org.jbpm.workbench.pr.model.ProcessInstanceSummary;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.uberfire.client.views.pfly.widgets.ConfirmPopup;
import org.uberfire.ext.services.shared.preferences.*;
import org.uberfire.ext.widgets.table.client.ColumnMeta;
import org.uberfire.mvp.Command;

import static org.jbpm.workbench.pr.client.editors.instance.list.ProcessInstanceListViewImpl.COL_ID_ACTIONS;
import static org.jbpm.workbench.pr.client.editors.instance.list.ProcessInstanceListViewImpl.COL_ID_SELECT;
import static org.jbpm.workbench.pr.model.ProcessInstanceDataSetConstants.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(GwtMockitoTestRunner.class)
public class ProcessInstanceListViewImplTest extends AbstractMultiGridViewTest<ProcessInstanceSummary> {

    @Mock
    private ProcessInstanceListPresenter presenter;

    @Spy
    private GenericErrorSummaryCountCell cellMock;

    @Mock
    private ManagedInstance<GenericErrorSummaryCountCell> popoverCellInstance;

    @Mock
    private ConfirmPopup confirmPopup;

    @GwtMock
    private AnchorListItem anchorListItem;

    @InjectMocks
    @Spy
    private ProcessInstanceListViewImpl view;

    @Override
    protected AbstractMultiGridView getView() {
        return view;
    }

    @Override
    protected AbstractMultiGridPresenter getPresenter() {
        return presenter;
    }

    @Override
    public List<String> getExpectedInitialColumns() {
        return Arrays.asList(COL_ID_SELECT,
                             COLUMN_PROCESS_INSTANCE_ID,
                             COLUMN_PROCESS_NAME,
                             COLUMN_PROCESS_INSTANCE_DESCRIPTION,
                             COLUMN_PROCESS_VERSION,
                             COLUMN_LAST_MODIFICATION_DATE,
                             COLUMN_ERROR_COUNT,
                             COL_ID_ACTIONS);
    }

    @Override
    public List<String> getExpectedBannedColumns() {
        return Arrays.asList(COL_ID_SELECT,
                             COLUMN_PROCESS_INSTANCE_ID,
                             COLUMN_PROCESS_NAME,
                             COLUMN_PROCESS_INSTANCE_DESCRIPTION,
                             COL_ID_ACTIONS);
    }

    @Override
    public Integer getExpectedNumberOfColumns() {
        return 15;
    }

    @Before
    @Override
    public void setupMocks() {
        super.setupMocks();
        when(popoverCellInstance.get()).thenReturn(cellMock);
    }

    @Test
    public void testSignalCommand() {
        final ExtendedPagedTable table = mock(ExtendedPagedTable.class);

        view.getSignalCommand(table).execute();

        verify(presenter).bulkSignal(any());
    }

    @Test
    public void testBulkSignal() {
        doAnswer(invocation -> {
            ClickHandler handler = (ClickHandler) invocation.getArguments()[0];
            handler.onClick(mock(ClickEvent.class));
            return null;
        }).when(anchorListItem).addClickHandler(any());
        final ExtendedPagedTable table = mock(ExtendedPagedTable.class);

        view.getBulkSignal(table);

        verify(presenter).bulkSignal(any());
    }

    @Test
    public void testBulkAbort() {
        doAnswer(invocation -> {
            ClickHandler handler = (ClickHandler) invocation.getArguments()[0];
            handler.onClick(mock(ClickEvent.class));
            return null;
        }).when(anchorListItem).addClickHandler(any());
        final ExtendedPagedTable table = mock(ExtendedPagedTable.class);

        view.getBulkAbort(table);

        ArgumentCaptor<Command> captor = ArgumentCaptor.forClass(Command.class);
        verify(confirmPopup).show(any(),
                                  any(),
                                  any(),
                                  captor.capture());

        captor.getValue().execute();

        verify(presenter).bulkAbort(any());
    }

    @Test
    public void testAbortCommand() {
        final ExtendedPagedTable table = mock(ExtendedPagedTable.class);

        view.getAbortCommand(table).execute();

        verify(presenter).bulkAbort(any());
    }

    @Test
    public void addDomainSpecifColumnsTest() {
        final ListTable<ProcessInstanceSummary> currentListGrid = spy(new ListTable<>(new GridGlobalPreferences()));
        when(view.getListGrid()).thenReturn(currentListGrid);
        final Set<String> domainColumns = new HashSet<String>(Arrays.asList("var1", "var2", "var3"));
        getView().addDomainSpecifColumns(domainColumns);

        final ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        verify(currentListGrid, times(3)).addColumns(argument.capture());

        final List<List> columnsList = argument.getAllValues();

        assertThat(columnsList.size()).isEqualTo(3);
        assertThat(columnsList.stream()).extracting(columns -> ((ColumnMeta) columns.get(0)).getCaption())
                .contains("var3", Index.atIndex(0))
                .contains("var2", Index.atIndex(1))
                .contains("var1", Index.atIndex(2));
    }

    @Test
    public void testRenameProcessVariableForInitColumns() {
        GridGlobalPreferences gridPreferences = new GridGlobalPreferences("test", view.getInitColumns(), view.getBannedColumns());

        ListTable<ProcessInstanceSummary> extendedPagedTable = new ListTable<ProcessInstanceSummary>(gridPreferences);
        List<GridColumnPreference> gridColumnPreferenceList = extendedPagedTable.getGridPreferencesStore().getColumnPreferences();
        gridColumnPreferenceList.add(new GridColumnPreference("Name", -1, ""));
        gridColumnPreferenceList.add(new GridColumnPreference("Id", -1, ""));
        gridColumnPreferenceList.add(new GridColumnPreference("performance", -1, ""));

        final ColumnMeta checkColumnMeta = view.initChecksColumn(extendedPagedTable);
        ColumnMeta<ProcessInstanceSummary> actionsColumnMeta = view.initActionsColumn();
        Column<ProcessInstanceSummary, ?> errorCountColumn = view.initErrorCountColumn();
        extendedPagedTable.addSelectionIgnoreColumn(checkColumnMeta.getColumn());
        extendedPagedTable.addSelectionIgnoreColumn(actionsColumnMeta.getColumn());
        extendedPagedTable.addSelectionIgnoreColumn(errorCountColumn);
        final Column<ProcessInstanceSummary, String> startColumn = view.createTextColumn(COLUMN_START,
                                                                                         process -> DateUtils.getDateTimeStr(process.getStartTime()));

        List<ColumnMeta<ProcessInstanceSummary>> columnMetas = view.getGeneralColumnMetas(extendedPagedTable,
                                                                                          startColumn,
                                                                                          checkColumnMeta,
                                                                                          actionsColumnMeta,
                                                                                          errorCountColumn);

        assertThat(columnMetas.stream()).extracting(columnMeta -> columnMeta.getCaption()).hasSize(getExpectedNumberOfColumns()).doesNotContain("Var_Name")
                .doesNotContain("Var_Id");

        columnMetas.addAll(view.renameVariables(extendedPagedTable, columnMetas));

        assertThat(columnMetas.stream()).extracting(columnMeta -> columnMeta.getCaption()).hasSize(getExpectedNumberOfColumns() + 3).containsOnlyOnce("Var_Name")
                .containsOnlyOnce("Var_Id");
    }

    @Test
    public void testRenameProcessVariableForAddDomainSpecifColumns() {
        GridGlobalPreferences gridPreferences = new GridGlobalPreferences("test", view.getInitColumns(), view.getBannedColumns());

        ListTable<ProcessInstanceSummary> extendedPagedTable = new ListTable<ProcessInstanceSummary>(gridPreferences);
        when(view.getListGrid()).thenReturn(extendedPagedTable);

        Set<String> set = Collections.singleton("Version");
        view.initColumns(extendedPagedTable);

        assertThat(extendedPagedTable.getColumnMetaList()).extracting(columnMeta -> columnMeta.getCaption()).hasSize(getExpectedNumberOfColumns()).doesNotContain("Var_Version");

        view.addDomainSpecifColumns(set);

        assertThat(extendedPagedTable.getColumnMetaList()).extracting(columnMeta -> columnMeta.getCaption()).hasSize(getExpectedNumberOfColumns() + 1).containsOnlyOnce("Var_Version");
    }

    @Test
    public void testRemoveColumnMetaFromColumnsForAddDomainSpecifColumns() {
        GridGlobalPreferences gridPreferences = new GridGlobalPreferences("test", view.getInitColumns(), view.getBannedColumns());

        ListTable<ProcessInstanceSummary> extendedPagedTable = new ListTable<ProcessInstanceSummary>(gridPreferences);
        when(view.getListGrid()).thenReturn(extendedPagedTable);

        extendedPagedTable.getGridPreferencesStore().getColumnPreferences().add(new GridColumnPreference("Extra", -1, ""));

        Set<String> set = Sets.newHashSet("Extra");

        view.initColumns(extendedPagedTable);
        assertThat(extendedPagedTable.getColumnMetaList().size()).isEqualTo(getExpectedNumberOfColumns() + 1);

        view.addDomainSpecifColumns(set);

        assertThat(set.size()).isEqualTo(0);
    }

    @Test
    public void testRemoveColumnMetaFromExtendedPagedTableForAddDomainSpecifColumns() {
        GridGlobalPreferences gridPreferences = new GridGlobalPreferences("test", view.getInitColumns(), view.getBannedColumns());

        ListTable<ProcessInstanceSummary> extendedPagedTable = new ListTable<ProcessInstanceSummary>(gridPreferences);
        when(view.getListGrid()).thenReturn(extendedPagedTable);

        extendedPagedTable.getGridPreferencesStore().getColumnPreferences().add(new GridColumnPreference("Extra", -1, ""));

        Column<ProcessInstanceSummary, String> column = view.createTextColumn("Extra", process -> process.getDeploymentId());
        Set<String> set = Collections.singleton("Extra_test");

        view.initColumns(extendedPagedTable);

        assertThat(extendedPagedTable.getColumnMetaList()).hasSize(getExpectedNumberOfColumns() + 1).extracting(columnMeta -> columnMeta.getCaption()).containsOnlyOnce("Extra");
        view.addDomainSpecifColumns(set);

        assertThat(extendedPagedTable.getColumnMetaList()).hasSize(getExpectedNumberOfColumns() + 1).extracting(columnMeta -> columnMeta.getCaption()).doesNotContain("Extra").containsOnlyOnce("Extra_test");
        assertThat(set.size()).isEqualTo(1);
    }

    @Test
    public void removeDomainSpecifColumnsTest() {
        final ListTable<ProcessInstanceSummary> listGrid = spy(new ListTable<>(new GridGlobalPreferences()));
        final ColumnMeta columnToRemove = new ColumnMeta<>(newColumnMock("c1"), "", true, true);
        listGrid.getColumnMetaList().add(columnToRemove);
        listGrid.getColumnMetaList().add(new ColumnMeta<>(newColumnMock("c2"), "", false, true));
        listGrid.getColumnMetaList().add(new ColumnMeta<>(newColumnMock("c3"), "", true, false));
        listGrid.getColumnMetaList().add(new ColumnMeta<>(newColumnMock("c4"), "", false, false));
        listGrid.getGridPreferencesStore().setPreferenceKey("key");
        when(getView().getListGrid()).thenReturn(listGrid);
        doNothing().when(listGrid).removeColumnMeta(any());
        final GridPreferencesStore store = new GridPreferencesStore(new GridGlobalPreferences());
        store.getColumnPreferences().add(new GridColumnPreference("c3", 0, ""));
        store.getColumnPreferences().add(new GridColumnPreference("c1", 1, ""));

        when(userPreferencesServiceMock.loadUserPreferences(listGrid.getGridPreferencesStore().getPreferenceKey(), UserPreferencesType.GRIDPREFERENCES)).thenReturn(store);

        getView().removeDomainSpecifColumns();

        assertEquals(1, store.getColumnPreferences().size());
        assertEquals("c3", store.getColumnPreferences().get(0).getName());
        verify(listGrid).removeColumnMeta(columnToRemove);
        verify(listGrid).saveGridPreferences();
    }
}
