/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {Component, OnInit} from '@angular/core';
import {MatDialog, MatDialogRef} from '@angular/material';
import {AddWidgetDialogComponent} from '../add-widget-dialog/add-widget-dialog.component';
import {ActivatedRoute} from '@angular/router';
import {AddDashboardDialogComponent} from '../../../../../modules/home/components/add-dashboard-dialog/add-dashboard-dialog.component';
import {TvManagementDialogComponent} from '../tv-management-dialog/tv-management-dialog.component';
import {ScreenService} from '../../../../../modules/dashboard/screen.service';
import {DashboardService} from '../../../../../modules/dashboard/dashboard.service';
import {Project} from '../../../../model/dto/Project';

/**
 * Hold the header dashboard actions
 */
@Component({
  selector: 'app-dashboard-actions',
  templateUrl: './dashboard-actions.component.html',
  styleUrls: ['./dashboard-actions.component.css']
})
export class DashboardActionsComponent implements OnInit {

  /**
   * Dialog reference used for add a widget
   * @type {MatDialogRef<AddWidgetDialogComponent>}
   */
  addWidgetDialogRef: MatDialogRef<AddWidgetDialogComponent>;

  /**
   * Dialog reference used for edit a dashboard
   * @type {MatDialogRef<AddDashboardDialogComponent>}
   */
  editDashboardDialogRef: MatDialogRef<AddDashboardDialogComponent>;

  /**
   * Dialog reference used for TV Management
   * @type {MatDialogRef<TvManagementDialogComponent>}
   */
  tvManagementDialogRef: MatDialogRef<TvManagementDialogComponent>;

  /**
   * The current project id
   * @type {Project}
   */
  project: Project;

  /**
   * The constructor
   *
   * @param {MatDialog} _matDialog The mat dialog to inject
   * @param {ActivatedRoute} _activatedRoute The activated route
   * @param {ScreenService} _screenService The screen service
   * @param {DashboardService} _dashboardService The dashboard service
   */
  constructor(private _matDialog: MatDialog,
              private _activatedRoute: ActivatedRoute,
              private _screenService: ScreenService,
              private _dashboardService: DashboardService) {
  }

  /**
   * When the component is init
   */
  ngOnInit() {
    this._dashboardService.currentDisplayedDashboard$.subscribe(project => this.project = project);
  }

  /**
   * Open the Add widget dialog
   */
  openAddWidgetDialog() {
    this.addWidgetDialogRef = this._matDialog.open(AddWidgetDialogComponent, {
      minWidth: 900,
      data: {projectId: this.project.id}
    });
  }

  /**
   * Open the edit widget dialog
   */
  openEditDashboardDialog() {
    this.editDashboardDialogRef = this._matDialog.open(AddDashboardDialogComponent, {
      minWidth: 900,
      data: {projectId: this.project.id}
    });
  }

  /**
   * Open the tv management dialog
   */
  openTvManagementDialog() {
    this.tvManagementDialogRef = this._matDialog.open(TvManagementDialogComponent, {
      minWidth: 900,
      data: {projectId: this.project.id}
    });
  }

  /**
   * Refresh every screens for the current dashboard
   */
  refreshConnectedScreens() {
    this._screenService.refreshEveryConnectedScreensForProject(this.project.token);
  }
}
