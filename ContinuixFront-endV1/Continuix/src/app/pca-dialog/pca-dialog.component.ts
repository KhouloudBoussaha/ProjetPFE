import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PCA } from '../models/PCA';

@Component({
  selector: 'app-pca-dialog',
  templateUrl: './pca-dialog.component.html',
  styleUrls: ['./pca-dialog.component.css']
})
export class PcaDialogComponent {
   @Input() pca!: PCA;
  @Output() close = new EventEmitter<void>();

  closeDialog() {
    this.close.emit();
  }

}
