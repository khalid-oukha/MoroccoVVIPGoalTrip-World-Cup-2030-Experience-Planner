import { Component, OnInit } from '@angular/core';
import { TableComponent } from '../../components/table/table.component';
import { CitiesService } from '../../../../core/services/cities.service';
import { City } from '../../../../core/models/City';
import { FormField, GenericFormComponent } from '../../components/generic-form/generic-form.component';
import { NgIf, NgFor } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-cities',
  standalone: true,
  imports: [TableComponent, GenericFormComponent, NgIf, NgFor, FormsModule],
  templateUrl: './cities.component.html',
  styleUrl: './cities.component.scss',
})
export class CitiesComponent implements OnInit {
  cities: City[] = [];
  showForm = false;
  selectedCity: City | null = null;
  isLoading = false;
  selectedRegion: string = '';

  // Dynamically populated regions
  regions: string[] = [];

  formFields: FormField[] = [
    { key: 'name', label: 'City Name', type: 'text', required: true },
    {
      key: 'region',
      label: 'Region',
      type: 'select',
      required: true,
      options: [] // Will be populated after loading cities
    }
  ];

  columns = [
    { key: 'name', header: 'City Name' },
    { key: 'region', header: 'Region' },
    { key: 'actions', header: 'Actions' }
  ];

  constructor(private citiesService: CitiesService) {}

  ngOnInit() {
    this.loadCities();
  }

  loadCities() {
    this.isLoading = true;
    this.citiesService.findAll(this.selectedRegion || undefined).subscribe({
      next: (cities) => {
        this.cities = cities;
        this.isLoading = false;

        // Extract unique regions from cities
        this.extractUniqueRegions();
      },
      error: (err) => {
        console.error('Error loading cities:', err);
        this.isLoading = false;
      }
    });
  }

  extractUniqueRegions() {
    // Get all cities (without region filter) to extract all possible regions
    this.citiesService.findAll().subscribe({
      next: (allCities) => {
        // Extract unique regions
        this.regions = [...new Set(allCities.map(city => city.region))].sort();

        // Update form field options
        this.updateRegionOptions();
      },
      error: (err) => console.error('Error loading all cities for regions:', err)
    });
  }

  updateRegionOptions() {
    // Update the region options in the form
    const regionField = this.formFields.find(field => field.key === 'region');
    if (regionField) {
      regionField.options = this.regions.map(region => ({
        value: region,
        label: region
      }));
    }
  }

  handleEdit(city: City) {
    this.selectedCity = { ...city };
    this.showForm = true;
  }

  handleDelete(city: City) {
    if (city.id && confirm(`Delete ${city.name}?`)) {
      this.isLoading = true;
      this.citiesService.delete(city.id).subscribe({
        next: () => {
          this.loadCities();
        },
        error: (err) => {
          console.error('Delete failed:', err);
          this.isLoading = false;
          alert('Failed to delete city');
        }
      });
    }
  }

  handleFormSubmit(formData: any) {
    this.isLoading = true;

    // Create a city object from form data
    const cityData: City = {
      name: formData.name.trim(),
      region: formData.region
    };

    const operation = this.selectedCity && this.selectedCity.id
      ? this.citiesService.update(this.selectedCity.id, cityData)
      : this.citiesService.create(cityData);

    operation.subscribe({
      next: () => {
        this.showForm = false;
        this.selectedCity = null;
        this.loadCities();
      },
      error: (err) => {
        console.error('Save failed:', err);
        this.isLoading = false;
        alert('Failed to save city');
      }
    });
  }

  handleFormCancel() {
    this.showForm = false;
    this.selectedCity = null;
  }

  onRegionChange() {
    this.loadCities();
  }
}
